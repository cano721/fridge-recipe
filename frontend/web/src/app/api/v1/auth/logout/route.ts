import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse } from '@/lib/auth';

export async function POST(request: NextRequest) {
  try {
    if (!isSupabaseConfigured) return successResponse({ message: '로그아웃 되었습니다.' });

    const userId = requireUserId(request);
    const supabase = getServiceSupabase();

    await supabase.from('refresh_tokens')
      .update({ revoked_at: new Date().toISOString() })
      .eq('user_id', userId).is('revoked_at', null);

    return successResponse({ message: '로그아웃 되었습니다.' });
  } catch (e) {
    if ((e as Error).name === 'AuthError') return errorResponse('UNAUTHORIZED', (e as Error).message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
