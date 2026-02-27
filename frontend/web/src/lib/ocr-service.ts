/**
 * GPT-4o Vision 기반 영수증 OCR
 * 이미지에서 직접 식재료를 추출 (OCR + 파싱 통합)
 * OPENAI_API_KEY 미설정 시 mock 반환
 */

const OPENAI_API_KEY = process.env.OPENAI_API_KEY || '';

export interface ParsedItem {
  name: string;
  quantity: number;
  unit: string;
  confidence: number;
}

const SYSTEM_PROMPT =
  '당신은 영수증 이미지에서 식재료를 추출하는 전문가입니다. ' +
  '다음 규칙을 따르세요:\n' +
  '1. 식재료만 추출 (봉투, 비닐, 포인트, 할인, 쿠폰, 카드, 현금, 부가세, 합계, 거스름, 영수증 등 제외)\n' +
  '2. 브랜드명(풀무원, CJ, 오뚜기, 농심, 롯데, 동원, 해태 등)과 중량 표기는 제거하고 식재료명만 추출\n' +
  '3. 수량과 단위를 추론 (기본값: 1개)\n' +
  '4. JSON으로만 응답: {"items": [{"name": "식재료명", "quantity": 1, "unit": "개", "confidence": 0.9}]}\n' +
  '5. confidence는 인식 확신도 (0.0~1.0)\n' +
  '6. 식재료가 하나도 없으면 {"items": []}';

async function callGPTVision(imageBase64: string): Promise<ParsedItem[]> {
  const imageUrl = imageBase64.startsWith('data:')
    ? imageBase64
    : `data:image/jpeg;base64,${imageBase64}`;

  const payload = {
    model: 'gpt-4o-mini',
    messages: [
      { role: 'system', content: SYSTEM_PROMPT },
      {
        role: 'user',
        content: [
          { type: 'image_url', image_url: { url: imageUrl } },
          { type: 'text', text: '이 영수증에서 식재료를 추출해주세요.' },
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
  return parsed.items ?? parsed.ingredients ?? [];
}

function mockResult(): ParsedItem[] {
  return [
    { name: '두부', quantity: 1, unit: '모', confidence: 0.95 },
    { name: '계란', quantity: 1, unit: '판', confidence: 0.92 },
    { name: '양파', quantity: 3, unit: '개', confidence: 0.88 },
    { name: '돼지고기', quantity: 500, unit: 'g', confidence: 0.85 },
  ];
}

export async function recognizeReceipt(imageBase64: string): Promise<ParsedItem[]> {
  if (!OPENAI_API_KEY) {
    return mockResult();
  }

  try {
    return await callGPTVision(imageBase64);
  } catch (e) {
    console.error('Receipt OCR failed, returning mock:', (e as Error).message);
    return mockResult();
  }
}
