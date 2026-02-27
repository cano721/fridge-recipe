/**
 * GPT-4o Vision API로 사진에서 식재료 인식
 * Python backend/ai-service/app/ml/vision/openai_vision.py 변환
 */

const OPENAI_API_KEY = process.env.OPENAI_API_KEY || '';

const SYSTEM_PROMPT =
  '당신은 냉장고 식재료 인식 전문가입니다. ' +
  '사진에서 보이는 식재료를 인식하여 JSON 배열로만 응답하세요. ' +
  '형식: [{"name": "식재료명", "confidence": 0.0~1.0}] ' +
  '식재료가 아닌 물건은 제외하세요.';

export interface VisionItem {
  name: string;
  confidence: number;
}

export async function recognizeIngredients(imageUrl: string): Promise<VisionItem[]> {
  if (!OPENAI_API_KEY) {
    return [{ name: '식재료 인식 미설정', confidence: 0.0 }];
  }

  try {
    return await callVisionAPI(imageUrl);
  } catch (e) {
    console.error('Vision API failed, returning mock:', (e as Error).message);
    return [{ name: '식재료 인식 미설정', confidence: 0.0 }];
  }
}

async function callVisionAPI(imageUrl: string): Promise<VisionItem[]> {
  const payload = {
    model: 'gpt-4o',
    messages: [
      { role: 'system', content: SYSTEM_PROMPT },
      {
        role: 'user',
        content: [
          { type: 'image_url', image_url: { url: imageUrl } },
          { type: 'text', text: '이 사진에서 식재료를 인식해주세요.' },
        ],
      },
    ],
    max_tokens: 512,
    response_format: { type: 'json_object' },
  };

  const res = await fetch('https://api.openai.com/v1/chat/completions', {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${OPENAI_API_KEY}`,
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(payload),
    signal: AbortSignal.timeout(30000),
  });

  if (!res.ok) throw new Error(`OpenAI API error: ${res.status}`);

  const data = await res.json();
  const content = data.choices[0].message.content;
  const parsed = JSON.parse(content);

  if (Array.isArray(parsed)) return parsed;
  return parsed.ingredients ?? parsed.items ?? [];
}

