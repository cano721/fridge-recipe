"""GPT-4o Vision API로 사진에서 식재료 인식"""
import json

import httpx

from app.core.config import settings

SYSTEM_PROMPT = (
    "당신은 냉장고 식재료 인식 전문가입니다. "
    "사진에서 보이는 식재료를 인식하여 JSON 배열로만 응답하세요. "
    "형식: [{\"name\": \"식재료명\", \"confidence\": 0.0~1.0}] "
    "식재료가 아닌 물건은 제외하세요."
)


class VisionClient:
    async def recognize_ingredients(self, image_url: str) -> list[dict]:
        """이미지 URL → GPT-4o Vision → 식재료 목록"""
        if not settings.openai_api_key:
            return self._mock_result()

        payload = {
            "model": "gpt-4o",
            "messages": [
                {"role": "system", "content": SYSTEM_PROMPT},
                {
                    "role": "user",
                    "content": [
                        {"type": "image_url", "image_url": {"url": image_url}},
                        {"type": "text", "text": "이 사진에서 식재료를 인식해주세요."},
                    ],
                },
            ],
            "max_tokens": 512,
            "response_format": {"type": "json_object"},
        }
        headers = {
            "Authorization": f"Bearer {settings.openai_api_key}",
            "Content-Type": "application/json",
        }

        async with httpx.AsyncClient(timeout=30.0) as client:
            response = await client.post(
                "https://api.openai.com/v1/chat/completions",
                json=payload,
                headers=headers,
            )
            response.raise_for_status()
            data = response.json()

        content = data["choices"][0]["message"]["content"]
        parsed = json.loads(content)
        if isinstance(parsed, list):
            return parsed
        return parsed.get("ingredients", parsed.get("items", []))

    def _mock_result(self) -> list[dict]:
        """OpenAI API 미설정 시 mock 결과"""
        return [{"name": "식재료 인식 미설정", "confidence": 0.0}]
