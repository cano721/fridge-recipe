"""영수증 상품명에서 식재료를 매칭하는 파서."""

import logging
import re

logger = logging.getLogger(__name__)

# 비식재료 키워드 (필터링 대상)
NON_FOOD_KEYWORDS = [
    "봉투", "비닐", "키친타올", "화장지", "휴지", "세제", "샴푸",
    "칫솔", "치약", "린스", "바디워시", "면도기", "배터리", "건전지",
    "행주", "수세미", "고무장갑", "쓰레기", "마스크", "밴드",
]

# 브랜드명 제거 패턴
BRAND_PATTERNS = [
    r"^(CJ|풀무원|오뚜기|동원|사조|대상|농심|삼양|롯데|해태|비비고|종가집)\s*",
    r"\s*(대용량|기획|묶음|세트|증정|할인)$",
    r"\s*\d+[gGmMlLkK]+$",  # 용량 제거
]


class ReceiptParser:
    """영수증 OCR 결과에서 식재료를 추출하고 마스터 DB와 매칭합니다."""

    def __init__(self, ingredient_names: list[str] | None = None):
        self.ingredient_names = ingredient_names or []

    def is_food_item(self, item_name: str) -> bool:
        """비식재료 여부를 판단합니다."""
        name_lower = item_name.lower().strip()
        return not any(keyword in name_lower for keyword in NON_FOOD_KEYWORDS)

    def clean_product_name(self, raw_name: str) -> str:
        """브랜드명, 용량 등을 제거하여 식재료명만 추출합니다."""
        cleaned = raw_name.strip()
        for pattern in BRAND_PATTERNS:
            cleaned = re.sub(pattern, "", cleaned).strip()
        return cleaned

    def match_ingredient(self, cleaned_name: str) -> tuple[str | None, str]:
        """정제된 상품명을 식재료 마스터와 매칭합니다.

        Returns:
            (matched_name, confidence): 매칭된 식재료명과 신뢰도
        """
        if not self.ingredient_names:
            return None, "low"

        # Exact match
        for name in self.ingredient_names:
            if name in cleaned_name or cleaned_name in name:
                return name, "high"

        return None, "low"

    def parse_items(self, ocr_items: list[dict]) -> list[dict]:
        """OCR 추출 항목들을 식재료로 변환합니다."""
        results = []
        for item in ocr_items:
            raw_text = item.get("raw_text", "")
            is_food = self.is_food_item(raw_text)
            cleaned = self.clean_product_name(raw_text)
            matched_name, confidence = self.match_ingredient(cleaned) if is_food else (None, "low")

            results.append(
                {
                    "raw_text": raw_text,
                    "cleaned_name": cleaned,
                    "matched_ingredient_name": matched_name,
                    "quantity": item.get("quantity", "1"),
                    "confidence": confidence,
                    "is_food": is_food,
                }
            )
        return results
