import { NextRequest } from 'next/server';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';

/**
 * 하위호환용 폴링 엔드포인트
 * 새 scan API는 즉시 결과를 반환하므로 이 엔드포인트는 더 이상 필요하지 않지만,
 * 기존 클라이언트 호환을 위해 유지합니다.
 */
export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ taskId: string }> },
) {
  try {
    requireUserId(request);
    const { taskId } = await params;

    return successResponse({
      taskId,
      status: 'done',
      items: [],
      message: 'Scan API now returns results immediately. Use POST /api/v1/scan instead.',
    });
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
