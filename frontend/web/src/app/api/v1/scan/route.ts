import { NextRequest } from 'next/server';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';
import { recognizeReceipt } from '@/lib/ocr-service';
import { recognizeIngredients } from '@/lib/vision-service';

export async function POST(request: NextRequest) {
  try {
    requireUserId(request);
    const body = await request.json();
    const { type, image } = body as { type: 'receipt' | 'photo'; image: string };

    if (!type || !image) {
      return errorResponse('BAD_REQUEST', 'type and image are required', 400);
    }

    if (type === 'receipt') {
      const items = await recognizeReceipt(image);
      return successResponse({ status: 'done', items, type: 'receipt' });
    }

    // photo
    const ingredients = await recognizeIngredients(image);
    const items = ingredients.map((ing) => ({
      name: ing.name,
      confidence: ing.confidence,
    }));
    return successResponse({ status: 'done', items, type: 'photo' });
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
