import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { createToken, successResponse, errorResponse } from '@/lib/auth';
import { mockUser } from '@/lib/mock-data';
import crypto from 'crypto';

export async function POST(request: NextRequest) {
  try {
    const { refreshToken, deviceInfo } = await request.json();
    if (!refreshToken) return errorResponse('VALIDATION_FAILED', 'refreshToken은 필수입니다.');

    if (!isSupabaseConfigured) {
      const token = createToken({ userId: mockUser.id, email: mockUser.email });
      return successResponse({ accessToken: token, refreshToken: 'mock-refresh-token-new', expiresIn: 3600 });
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
      user_id: user.id, token_hash: newTokenHash, device_info: deviceInfo || null,
      expires_at: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString(),
    });

    return successResponse({ accessToken: newAccessToken, refreshToken: newRefreshToken, expiresIn });
  } catch (e) {
    console.error('[auth/refresh] Unexpected error:', e);
    return errorResponse('INTERNAL_ERROR', '서버 오류가 발생했습니다.', 500);
  }
}
