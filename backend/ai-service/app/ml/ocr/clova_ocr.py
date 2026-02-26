"""Naver Clova OCR API 클라이언트"""
import time
import uuid

import httpx

from app.core.config import settings


class ClovaOcrClient:
    def __init__(self):
        self.api_url = settings.clova_ocr_api_url
        self.secret_key = settings.clova_ocr_secret_key

    async def recognize_receipt(self, image_url: str) -> dict:
        """영수증 이미지 URL → Clova OCR API → raw result"""
        if not self.api_url or not self.secret_key:
            return self._mock_result()

        payload = {
            "images": [
                {
                    "format": "jpg",
                    "url": image_url,
                    "name": "receipt",
                }
            ],
            "requestId": str(uuid.uuid4()),
            "timestamp": int(time.time() * 1000),
            "version": "V2",
        }
        headers = {
            "X-OCR-SECRET": self.secret_key,
            "Content-Type": "application/json",
        }

        async with httpx.AsyncClient(timeout=30.0) as client:
            response = await client.post(self.api_url, json=payload, headers=headers)
            response.raise_for_status()
            return response.json()

    def _mock_result(self) -> dict:
        """Clova OCR API 미설정 시 mock 결과"""
        return {
            "images": [
                {
                    "receipt": {
                        "result": {
                            "storeInfo": {"name": {"text": "테스트마트"}},
                            "subResults": [
                                {
                                    "items": [
                                        {
                                            "name": {"text": "두부", "formatted": {"value": "두부"}},
                                            "count": {"text": "1"},
                                            "price": {"unitPrice": {"text": "1500"}},
                                        },
                                        {
                                            "name": {"text": "계란 10구", "formatted": {"value": "계란 10구"}},
                                            "count": {"text": "1"},
                                            "price": {"unitPrice": {"text": "3000"}},
                                        },
                                        {
                                            "name": {"text": "비닐봉투", "formatted": {"value": "비닐봉투"}},
                                            "count": {"text": "1"},
                                            "price": {"unitPrice": {"text": "100"}},
                                        },
                                    ]
                                }
                            ],
                        }
                    }
                }
            ]
        }
