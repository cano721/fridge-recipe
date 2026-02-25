"""GPT-4o Vision API client for ingredient recognition."""

import json
import logging

from openai import AsyncOpenAI

from app.core.config import settings

logger = logging.getLogger(__name__)

SYSTEM_PROMPT = """당신은 식재료 인식 전문가입니다.
사용자가 보내는 사진에서 보이는 모든 식재료를 인식하고 JSON 배열로 반환해주세요.

응답 형식:
[
  {"name": "식재료 한국어명", "category": "카테고리", "confidence": 0.0~1.0},
  ...
]

카테고리: 채소, 과일, 육류, 해산물, 유제품, 달걀, 냉동식품, 조미료, 곡물, 기타

규칙:
- 한국어 식재료명으로 반환
- 동일 식재료는 하나만 포함
- confidence는 인식 확실성 (0.0~1.0)
- JSON 배열만 반환, 다른 텍스트 없이
"""


async def recognize_ingredients_from_image(image_base64: str) -> list[dict]:
    """GPT-4o Vision으로 이미지에서 식재료를 인식합니다."""
    client = AsyncOpenAI(api_key=settings.openai_api_key)

    try:
        response = await client.chat.completions.create(
            model=settings.openai_model,
            messages=[
                {"role": "system", "content": SYSTEM_PROMPT},
                {
                    "role": "user",
                    "content": [
                        {"type": "text", "text": "이 사진에서 보이는 모든 식재료를 인식해주세요."},
                        {
                            "type": "image_url",
                            "image_url": {"url": f"data:image/jpeg;base64,{image_base64}"},
                        },
                    ],
                },
            ],
            max_tokens=1000,
            temperature=0.1,
        )

        content = response.choices[0].message.content or "[]"
        # Strip markdown code fences if present
        content = content.strip()
        if content.startswith("```"):
            content = content.split("\n", 1)[1] if "\n" in content else content[3:]
        if content.endswith("```"):
            content = content[:-3]
        content = content.strip()

        ingredients = json.loads(content)
        return ingredients

    except Exception as e:
        logger.error(f"Vision API error: {e}")
        return []
