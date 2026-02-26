import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { createToken, successResponse, errorResponse } from '@/lib/auth';
import { mockUser } from '@/lib/mock-data';
import crypto from 'crypto';

export async function POST(request: NextRequest) {
  try {
    const { provider, accessToken, deviceInfo } = await request.json();

    if (!provider || !accessToken) {
      return errorResponse('VALIDATION_FAILED', 'provider와 accessToken은 필수입니다.');
    }

    // Mock mode
    if (!isSupabaseConfigured) {
      const token = createToken({ userId: mockUser.id, email: mockUser.email });
      return successResponse({ accessToken: token, refreshToken: 'mock-refresh-token', expiresIn: 3600 });
    }

    let oauthId: string;
    let email: string | null = null;
    let nickname: string;

    if (provider === 'demo') {
      oauthId = 'demo-user-001';
      email = 'demo@fridgerecipe.app';
      nickname = '냉장고 요리사';
    } else {
      oauthId = accessToken;
      nickname = `${provider} 사용자`;
    }

    const supabase = getServiceSupabase();
    const { data: user, error } = await supabase
      .from('users')
      .upsert(
        { oauth_provider: provider, oauth_id: oauthId, email, nickname },
        { onConflict: 'oauth_provider,oauth_id' }
      )
      .select('id, email, nickname, profile_image, oauth_provider')
      .single();

    if (error) return errorResponse('DB_ERROR', error.message, 500);

    const expiresIn = 3600;
    const jwtAccessToken = createToken({ userId: user.id, email: user.email }, expiresIn);
    const refreshToken = crypto.randomBytes(32).toString('hex');
    const refreshTokenHash = crypto.createHash('sha256').update(refreshToken).digest('hex');

    await supabase.from('refresh_tokens').insert({
      user_id: user.id,
      token_hash: refreshTokenHash,
      device_info: deviceInfo || null,
      expires_at: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString(),
    });

    return successResponse({ accessToken: jwtAccessToken, refreshToken, expiresIn });
  } catch (e) {
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
