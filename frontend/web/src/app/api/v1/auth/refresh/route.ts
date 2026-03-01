import { NextRequest, NextResponse } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { createToken, errorResponse, REFRESH_TOKEN_COOKIE_OPTIONS } from '@/lib/auth';
import { mockUser } from '@/lib/mock-data';
import crypto from 'crypto';

export async function POST(request: NextRequest) {
  try {
    // refreshToken을 쿠키에서 읽기 (기존 body 방식도 하위호환)
    let refreshToken = request.cookies.get('refreshToken')?.value;
    if (!refreshToken) {
      try {
        const body = await request.json();
        refreshToken = body.refreshToken;
      } catch { /* no body */ }
    }

    if (!refreshToken) return errorResponse('VALIDATION_FAILED', 'refreshToken은 필수입니다.');

    if (!isSupabaseConfigured) {
      const token = createToken({ userId: mockUser.id, email: mockUser.email });
      const response = NextResponse.json({
        success: true,
        data: { accessToken: token, expiresIn: 3600 },
      });
      response.cookies.set('refreshToken', 'mock-refresh-token-new', REFRESH_TOKEN_COOKIE_OPTIONS);
      return response;
    }

    const tokenHash = crypto.createHash('sha256').update(refreshToken).digest('hex');
    const supabase = getServiceSupabase();

    const { data: stored, error } = await supabase
      .from('refresh_tokens').select('id, user_id, expires_at, revoked_at')
      .eq('token_hash', tokenHash).single();

    if (error || !stored) return errorResponse('INVALID_TOKEN', '유효하지 않은 리프레시 토큰입니다.', 400);
    if (stored.revoked_at || new Date(stored.expires_at) < new Date()) return errorResponse('TOKEN_EXPIRED', '만료된 리프레시 토큰입니다.', 400);

    await supabase.from('refresh_tokens').update({ revoked_at: new Date().toISOString() }).eq('id', stored.id);

    const { data: user } = await supabase.from('users').select('id, email').eq('id', stored.user_id).single();
    if (!user) return errorResponse('USER_NOT_FOUND', '사용자를 찾을 수 없습니다.', 404);

    const expiresIn = 3600;
    const newAccessToken = createToken({ userId: user.id, email: user.email }, expiresIn);
    const newRefreshToken = crypto.randomBytes(32).toString('hex');
    const newTokenHash = crypto.createHash('sha256').update(newRefreshToken).digest('hex');

    await supabase.from('refresh_tokens').insert({
      user_id: user.id, token_hash: newTokenHash,
      expires_at: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString(),
    });

    const response = NextResponse.json({
      success: true,
      data: { accessToken: newAccessToken, expiresIn },
    });
    response.cookies.set('refreshToken', newRefreshToken, REFRESH_TOKEN_COOKIE_OPTIONS);
    return response;
  } catch (e) {
    console.error('[auth/refresh] Unexpected error:', e);
    return errorResponse('INTERNAL_ERROR', '서버 오류가 발생했습니다.', 500);
  }
}
