import { NextRequest } from 'next/server';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';
import { isSupabaseConfigured } from '@/lib/supabase';

const AI_SERVICE_URL = process.env.AI_SERVICE_URL;
const INTERNAL_API_KEY = process.env.INTERNAL_API_KEY || 'dev-internal-key';

const MOCK_RECEIPT_ITEMS = [
  { name: '계란', quantity: 1, unit: '판', confidence: 0.95 },
  { name: '우유', quantity: 1, unit: 'L', confidence: 0.92 },
  { name: '양파', quantity: 3, unit: '개', confidence: 0.88 },
  { name: '돼지고기', quantity: 500, unit: 'g', confidence: 0.85 },
  { name: '두부', quantity: 1, unit: '모', confidence: 0.82 },
];

const MOCK_PHOTO_ITEMS = [
  { name: '당근', confidence: 0.94 },
  { name: '감자', confidence: 0.91 },
  { name: '양파', confidence: 0.87 },
];

export async function POST(request: NextRequest) {
  try {
    const userId = requireUserId(request);
    const body = await request.json();
    const { type, image } = body as { type: 'receipt' | 'photo'; image: string };

    if (!type || !image) {
      return errorResponse('BAD_REQUEST', 'type and image are required', 400);
    }

    // Try AI service
    if (AI_SERVICE_URL) {
      const endpoint = type === 'receipt' ? '/ai/ocr/receipt' : '/ai/vision/ingredients';
      try {
        const res = await fetch(`${AI_SERVICE_URL}${endpoint}`, {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'X-Internal-Api-Key': INTERNAL_API_KEY,
          },
          body: JSON.stringify({ image_url: image, user_id: userId }),
          signal: AbortSignal.timeout(5000),
        });

        if (res.ok) {
          const data = await res.json();
          return successResponse({ taskId: data.task_id, status: 'processing' });
        }
      } catch {
        // Fall through to mock
      }
    }

    // Mock: return immediate result with a fake task ID
    const taskId = `mock-${type}-${Date.now()}`;
    const items = type === 'receipt' ? MOCK_RECEIPT_ITEMS : MOCK_PHOTO_ITEMS;

    // Store mock result in a simple cache via headers (will be read by poll endpoint)
    if (typeof globalThis !== 'undefined') {
      const cache = (globalThis as Record<string, unknown>).__scanCache as Map<string, unknown> ??
        new Map<string, unknown>();
      cache.set(taskId, { status: 'done', items, type });
      (globalThis as Record<string, unknown>).__scanCache = cache;
    }

    return successResponse({ taskId, status: 'processing' });
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
