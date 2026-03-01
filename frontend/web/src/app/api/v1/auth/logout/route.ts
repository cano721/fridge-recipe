import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse } from '@/lib/auth';
import crypto from 'crypto';

export async function POST(request: NextRequest) {
  try {
    if (!isSupabaseConfigured) return successResponse({ message: '로그아웃 되었습니다.' });

    const userId = requireUserId(request);
    const supabase = getServiceSupabase();

    let body: { refreshToken?: string } = {};
    try { body = await request.json(); } catch { /* no body */ }

    if (body.refreshToken) {
      const tokenHash = crypto.createHash('sha256').update(body.refreshToken).digest('hex');
      await supabase.from('refresh_tokens')
        .update({ revoked_at: new Date().toISOString() })
        .eq('user_id', userId).eq('token_hash', tokenHash);
    } else {
      await supabase.from('refresh_tokens')
        .update({ revoked_at: new Date().toISOString() })
        .eq('user_id', userId).is('revoked_at', null);
    }

    return successResponse({ message: '로그아웃 되었습니다.' });
  } catch (e) {
    if ((e as Error).name === 'AuthError') return errorResponse('UNAUTHORIZED', (e as Error).message, 401);
    console.error('[auth/logout] Unexpected error:', e);
    return errorResponse('INTERNAL_ERROR', '서버 오류가 발생했습니다.', 500);
  }
}
