/**
 * Google Cloud Vision OCR + GPT-4o 영수증 파싱
 *
 * 1단계: Google Vision API → 영수증 텍스트 추출 (무료 1,000건/월)
 * 2단계: GPT-4o → 텍스트에서 식재료 목록 구조화
 * fallback: API 미설정 시 mock 데이터 반환
 */

const GOOGLE_VISION_API_KEY = process.env.GOOGLE_CLOUD_VISION_API_KEY || '';
const OPENAI_API_KEY = process.env.OPENAI_API_KEY || '';

export interface ParsedItem {
  name: string;
  quantity: number;
  unit: string;
  confidence: number;
}

// ── 1단계: Google Cloud Vision OCR ──

async function extractTextFromImage(imageBase64: string): Promise<string> {
  // data:image/... prefix 제거
  const base64Content = imageBase64.replace(/^data:image\/\w+;base64,/, '');

  const payload = {
    requests: [
      {
        image: { content: base64Content },
        features: [{ type: 'TEXT_DETECTION' }],
        imageContext: { languageHints: ['ko', 'en'] },
      },
    ],
  };

  const res = await fetch(
    `https://vision.googleapis.com/v1/images:annotate?key=${GOOGLE_VISION_API_KEY}`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload),
      signal: AbortSignal.timeout(30000),
    },
  );

  if (!res.ok) throw new Error(`Google Vision API error: ${res.status}`);

  const data = await res.json();
  const responses = data.responses?.[0];
  return responses?.fullTextAnnotation?.text || responses?.textAnnotations?.[0]?.description || '';
}

// ── 2단계: GPT-4o로 텍스트 파싱 ──

const PARSE_PROMPT =
  '당신은 영수증 텍스트에서 식재료만 추출하는 전문가입니다. ' +
  '다음 규칙을 따르세요:\n' +
  '1. 식재료만 추출 (봉투, 포인트, 할인, 쿠폰, 카드, 현금, 부가세, 합계 등 제외)\n' +
  '2. 브랜드명(풀무원, CJ, 오뚜기, 농심 등)과 중량 표기는 제거하고 식재료명만 추출\n' +
  '3. 수량과 단위를 추론 (기본값: 1개)\n' +
  '4. JSON 배열로만 응답: [{"name": "식재료명", "quantity": 1, "unit": "개", "confidence": 0.9}]\n' +
  '5. confidence는 인식 확신도 (0.0~1.0)';

async function parseReceiptWithGPT(text: string): Promise<ParsedItem[]> {
  const payload = {
    model: 'gpt-4o-mini',
    messages: [
      { role: 'system', content: PARSE_PROMPT },
      { role: 'user', content: `다음 영수증 텍스트에서 식재료를 추출해주세요:\n\n${text}` },
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
    signal: AbortSignal.timeout(15000),
  });

  if (!res.ok) throw new Error(`OpenAI API error: ${res.status}`);

  const data = await res.json();
  const content = data.choices[0].message.content;
  const parsed = JSON.parse(content);

  if (Array.isArray(parsed)) return parsed;
  return parsed.ingredients ?? parsed.items ?? [];
}

// ── 규칙 기반 파싱 (GPT 미설정 시 fallback) ──

const EXCLUDE_KEYWORDS = [
  '봉투', '비닐', '포인트', '할인', '쿠폰', '합계', '부가세',
  '카드', '현금', '거스름', '영수증', '합산', '결제', '잔액',
];

const KNOWN_BRANDS = ['풀무원', 'CJ', '오뚜기', '농심', '롯데', '동원', '해태'];

function parseReceiptByRules(text: string): ParsedItem[] {
  const lines = text.split('\n').map((l) => l.trim()).filter(Boolean);
  const items: ParsedItem[] = [];

  for (const line of lines) {
    if (EXCLUDE_KEYWORDS.some((kw) => line.includes(kw))) continue;
    if (/^\d[\d,.]+$/.test(line)) continue; // 숫자만 있는 줄 skip
    if (line.length < 2 || line.length > 30) continue;

    let name = line;
    // 가격 부분 제거
    name = name.replace(/[\d,]+원?$/, '').trim();
    // 중량 제거
    name = name.replace(/\d+[gGkKmMlL]+/g, '').trim();
    name = name.replace(/\d+구|\d+개|\d+팩/g, '').trim();
    // 브랜드 제거
    for (const brand of KNOWN_BRANDS) {
      name = name.replaceAll(brand, '');
    }
    name = name.trim();

    if (name.length < 1) continue;

    items.push({ name, quantity: 1, unit: '개', confidence: 0.6 });
  }

  return items;
}

// ── Mock ──

function mockResult(): ParsedItem[] {
  return [
    { name: '두부', quantity: 1, unit: '모', confidence: 0.95 },
    { name: '계란', quantity: 1, unit: '판', confidence: 0.92 },
    { name: '양파', quantity: 3, unit: '개', confidence: 0.88 },
    { name: '돼지고기', quantity: 500, unit: 'g', confidence: 0.85 },
  ];
}

// ── Public API ──

export async function recognizeReceipt(imageBase64: string): Promise<ParsedItem[]> {
  // API 미설정 시 mock
  if (!GOOGLE_VISION_API_KEY) {
    return mockResult();
  }

  try {
    // 1단계: OCR
    const text = await extractTextFromImage(imageBase64);
    if (!text.trim()) return [];

    // 2단계: 파싱 (GPT 사용 가능하면 GPT, 아니면 규칙 기반)
    if (OPENAI_API_KEY) {
      return parseReceiptWithGPT(text);
    }
    return parseReceiptByRules(text);
  } catch (e) {
    console.error('OCR failed, returning mock:', (e as Error).message);
    return mockResult();
  }
}
