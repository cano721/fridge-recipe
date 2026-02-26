"""OCR 결과를 식재료 목록으로 파싱"""


class ReceiptParser:
    EXCLUDE_KEYWORDS = [
        "봉투", "비닐", "포인트", "할인", "쿠폰", "합계", "부가세",
        "카드", "현금", "거스름", "영수증",
    ]

    def parse(self, ocr_result: dict) -> list[dict]:
        """Clova OCR raw result → [{name, quantity, unit, confidence}]"""
        items = []
        images = ocr_result.get("images", [])
        if not images:
            return items

        receipt = images[0].get("receipt", {})
        result = receipt.get("result", {})
        sub_results = result.get("subResults", [])

        for sub in sub_results:
            for item in sub.get("items", []):
                name_text = item.get("name", {}).get("text", "").strip()
                if not name_text:
                    continue
                if self._should_exclude(name_text):
                    continue

                quantity = self._parse_quantity(item.get("count", {}).get("text", "1"))
                unit = self._infer_unit(name_text, quantity)
                confidence = self._calc_confidence(item)

                items.append({
                    "name": self._normalize_name(name_text),
                    "quantity": quantity,
                    "unit": unit,
                    "confidence": confidence,
                })

        return items

    def _should_exclude(self, name: str) -> bool:
        return any(kw in name for kw in self.EXCLUDE_KEYWORDS)

    def _parse_quantity(self, text: str) -> int:
        try:
            return max(1, int(text.strip()))
        except (ValueError, AttributeError):
            return 1

    def _infer_unit(self, name: str, quantity: int) -> str:
        if any(kw in name for kw in ["구", "팩", "봉", "병", "캔"]):
            return "개"
        if any(kw in name for kw in ["g", "kg", "ml", "L"]):
            return "g"
        return "개"

    def _normalize_name(self, name: str) -> str:
        """브랜드명, 중량 표기 등 제거하여 식재료명만 추출"""
        import re
        name = re.sub(r"\d+[gGkKmMlL]+", "", name)
        name = re.sub(r"\d+구|\d+개|\d+팩", "", name)
        known_brands = ["풀무원", "CJ", "오뚜기", "농심", "롯데", "동원", "해태"]
        for brand in known_brands:
            name = name.replace(brand, "")
        return name.strip()

    def _calc_confidence(self, item: dict) -> float:
        formatted = item.get("name", {}).get("formatted", {})
        if formatted.get("value"):
            return 0.9
        return 0.7
