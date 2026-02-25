"""Naver Clova OCR client for receipt scanning."""

import base64
import json
import logging
import time
import uuid

import httpx

from app.core.config import settings

logger = logging.getLogger(__name__)


class ClovaOCRClient:
    """Naver Clova Document OCR - Receipt model client."""

    def __init__(self):
        self.api_url = settings.clova_ocr_api_url
        self.secret_key = settings.clova_ocr_secret_key

    async def scan_receipt(self, image_base64: str) -> dict:
        """영수증 이미지를 Clova OCR API로 전송하여 결과를 반환합니다."""
        request_body = {
            "version": "V2",
            "requestId": str(uuid.uuid4()),
            "timestamp": int(time.time() * 1000),
            "images": [
                {
                    "format": "jpg",
                    "name": "receipt",
                    "data": image_base64,
                }
            ],
        }

        headers = {
            "X-OCR-SECRET": self.secret_key,
            "Content-Type": "application/json",
        }

        async with httpx.AsyncClient(timeout=30.0) as client:
            response = await client.post(self.api_url, json=request_body, headers=headers)
            response.raise_for_status()
            return response.json()

    def parse_receipt_items(self, ocr_result: dict) -> list[dict]:
        """OCR 결과에서 상품명, 수량, 단가를 추출합니다."""
        items = []
        try:
            images = ocr_result.get("images", [])
            if not images:
                return items

            receipt = images[0].get("receipt", {}).get("result", {})
            sub_results = receipt.get("subResults", [])

            for sub in sub_results:
                for item in sub.get("items", []):
                    name = item.get("name", {}).get("text", "")
                    count = item.get("count", {}).get("text", "1")
                    price = item.get("price", {}).get("price", {}).get("text", "")

                    if name:
                        items.append(
                            {
                                "raw_text": name,
                                "quantity": count,
                                "price": price,
                            }
                        )
        except (KeyError, IndexError) as e:
            logger.error(f"Error parsing receipt: {e}")

        return items
