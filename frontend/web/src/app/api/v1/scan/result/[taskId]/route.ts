import { NextRequest } from 'next/server';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';

const AI_SERVICE_URL = process.env.AI_SERVICE_URL;
const INTERNAL_API_KEY = process.env.INTERNAL_API_KEY || 'dev-internal-key';

export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ taskId: string }> },
) {
  try {
    requireUserId(request);
    const { taskId } = await params;

    // Mock task - return cached result
    if (taskId.startsWith('mock-')) {
      const cache = (globalThis as Record<string, unknown>).__scanCache as Map<string, unknown> | undefined;
      const cached = cache?.get(taskId) as Record<string, unknown> | undefined;

      if (cached) {
        return successResponse({
          taskId,
          status: 'done',
          items: cached.items,
        });
      }
      return successResponse({ taskId, status: 'done', items: [] });
    }

    // Real AI service polling
    if (!AI_SERVICE_URL) {
      return errorResponse('SERVICE_UNAVAILABLE', 'AI service not configured', 503);
    }

    // Determine endpoint based on task type (receipt or photo)
    const isReceipt = taskId.includes('receipt') || !taskId.includes('vision');
    const endpoint = isReceipt
      ? `/ai/ocr/receipt/${taskId}`
      : `/ai/vision/ingredients/${taskId}`;

    const res = await fetch(`${AI_SERVICE_URL}${endpoint}`, {
      headers: { 'X-Internal-Api-Key': INTERNAL_API_KEY },
      signal: AbortSignal.timeout(5000),
    });

    if (!res.ok) {
      return errorResponse('AI_SERVICE_ERROR', `AI service returned ${res.status}`, 502);
    }

    const data = await res.json();
    return successResponse({
      taskId,
      status: data.status,
      items: data.items ?? data.ingredients ?? [],
      error: data.error,
    });
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
