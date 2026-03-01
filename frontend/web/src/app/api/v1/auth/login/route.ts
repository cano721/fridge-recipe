import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { createToken, successResponse, errorResponse } from '@/lib/auth';
import { mockUser } from '@/lib/mock-data';
import crypto from 'crypto';

const SUPPORTED_PROVIDERS = ['kakao', 'google', 'demo'] as const;
type Provider = typeof SUPPORTED_PROVIDERS[number];

async function verifyOAuthToken(provider: Provider, accessToken: string): Promise<{ oauthId: string; email: string | null; nickname: string; profileImage: string | null } | null> {
  if (provider === 'demo') {
    return { oauthId: 'demo-user-001', email: 'demo@fridgerecipe.app', nickname: '냉장고 요리사', profileImage: null };
  }

  if (provider === 'kakao') {
    const res = await fetch('https://kapi.kakao.com/v2/user/me', {
      headers: { Authorization: `Bearer ${accessToken}` },
    });
    if (!res.ok) return null;
    const profile = await res.json();
    return {
      oauthId: String(profile.id),
      email: profile.kakao_account?.email || null,
      nickname: profile.kakao_account?.profile?.nickname || '카카오 사용자',
      profileImage: profile.kakao_account?.profile?.profile_image_url || null,
    };
  }

  if (provider === 'google') {
    const res = await fetch('https://www.googleapis.com/oauth2/v3/userinfo', {
      headers: { Authorization: `Bearer ${accessToken}` },
    });
    if (!res.ok) return null;
    const profile = await res.json();
    return {
      oauthId: profile.sub,
      email: profile.email || null,
      nickname: profile.name || 'Google 사용자',
      profileImage: profile.picture || null,
    };
  }

  return null;
}

export async function POST(request: NextRequest) {
  try {
    const { provider, accessToken, deviceInfo } = await request.json();

    if (!provider || !accessToken) {
      return errorResponse('VALIDATION_FAILED', 'provider와 accessToken은 필수입니다.');
    }

    if (!SUPPORTED_PROVIDERS.includes(provider)) {
      return errorResponse('UNSUPPORTED_PROVIDER', '지원하지 않는 provider입니다.', 400);
    }

    // Mock mode
    if (!isSupabaseConfigured) {
      const token = createToken({ userId: mockUser.id, email: mockUser.email });
      return successResponse({ accessToken: token, refreshToken: 'mock-refresh-token', expiresIn: 3600 });
    }

    const verified = await verifyOAuthToken(provider as Provider, accessToken);
    if (!verified) {
      return errorResponse('INVALID_TOKEN', '유효하지 않은 OAuth 토큰입니다.', 401);
    }

    const supabase = getServiceSupabase();
    const { data: user, error } = await supabase
      .from('users')
      .upsert(
        { oauth_provider: provider, oauth_id: verified.oauthId, email: verified.email, nickname: verified.nickname, profile_image: verified.profileImage },
        { onConflict: 'oauth_provider,oauth_id' }
      )
      .select('id, email, nickname, profile_image, oauth_provider')
      .single();

    if (error || !user) {
      console.error('[auth/login] DB error:', error);
      return errorResponse('DB_ERROR', '서버 오류가 발생했습니다.', 500);
    }

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
    console.error('[auth/login] Unexpected error:', e);
    return errorResponse('INTERNAL_ERROR', '서버 오류가 발생했습니다.', 500);
  }
}
