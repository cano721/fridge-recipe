import { NextRequest, NextResponse } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, errorResponse } from '@/lib/auth';
import crypto from 'crypto';

export async function POST(request: NextRequest) {
  try {
    if (!isSupabaseConfigured) {
      const response = NextResponse.json({ success: true, data: { message: '로그아웃 되었습니다.' } });
      response.cookies.set('refreshToken', '', { maxAge: 0, path: '/api/v1/auth' });
      return response;
    }

    const userId = requireUserId(request);
    const supabase = getServiceSupabase();

    // 쿠키에서 refreshToken 읽기 (단일 기기 로그아웃)
    const refreshToken = request.cookies.get('refreshToken')?.value;

    if (refreshToken) {
      const tokenHash = crypto.createHash('sha256').update(refreshToken).digest('hex');
      await supabase.from('refresh_tokens')
        .update({ revoked_at: new Date().toISOString() })
        .eq('user_id', userId).eq('token_hash', tokenHash);
    } else {
      await supabase.from('refresh_tokens')
        .update({ revoked_at: new Date().toISOString() })
        .eq('user_id', userId).is('revoked_at', null);
    }

    const response = NextResponse.json({ success: true, data: { message: '로그아웃 되었습니다.' } });
    response.cookies.set('refreshToken', '', { maxAge: 0, path: '/api/v1/auth' });
    return response;
  } catch (e) {
    if ((e as Error).name === 'AuthError') return errorResponse('UNAUTHORIZED', (e as Error).message, 401);
    console.error('[auth/logout] Unexpected error:', e);
    return errorResponse('INTERNAL_ERROR', '서버 오류가 발생했습니다.', 500);
  }
}
