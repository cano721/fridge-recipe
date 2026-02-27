import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { createToken } from '@/lib/auth';
import crypto from 'crypto';

const KAKAO_CLIENT_ID = process.env.KAKAO_CLIENT_ID || '';
const KAKAO_CLIENT_SECRET = process.env.KAKAO_CLIENT_SECRET || '';

function getBaseUrl(request: NextRequest) {
  const host = request.headers.get('host') || 'localhost:3000';
  const proto = request.headers.get('x-forwarded-proto') || 'http';
  return `${proto}://${host}`;
}

export async function GET(request: NextRequest) {
  const baseUrl = getBaseUrl(request);
  const { searchParams } = new URL(request.url);
  const code = searchParams.get('code');
  const error = searchParams.get('error');

  if (error || !code) {
    return Response.redirect(`${baseUrl}/login?error=kakao_denied`);
  }

  try {
    // Exchange code for access token
    const redirectUri = `${baseUrl}/api/v1/auth/kakao/callback`;
    const tokenParams = new URLSearchParams({
      grant_type: 'authorization_code',
      client_id: KAKAO_CLIENT_ID,
      redirect_uri: redirectUri,
      code,
      ...(KAKAO_CLIENT_SECRET ? { client_secret: KAKAO_CLIENT_SECRET } : {}),
    });

    const tokenRes = await fetch('https://kauth.kakao.com/oauth/token', {
      method: 'POST',
      headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      body: tokenParams.toString(),
    });

    if (!tokenRes.ok) {
      return Response.redirect(`${baseUrl}/login?error=kakao_token_failed`);
    }

    const tokenData = await tokenRes.json();

    // Get user profile
    const profileRes = await fetch('https://kapi.kakao.com/v2/user/me', {
      headers: { Authorization: `Bearer ${tokenData.access_token}` },
    });

    if (!profileRes.ok) {
      return Response.redirect(`${baseUrl}/login?error=kakao_profile_failed`);
    }

    const profile = await profileRes.json();
    const kakaoId = String(profile.id);
    const nickname = profile.kakao_account?.profile?.nickname || '카카오 사용자';
    const email = profile.kakao_account?.email || null;
    const profileImage = profile.kakao_account?.profile?.profile_image_url || null;

    // Upsert user
    if (!isSupabaseConfigured) {
      const jwt = createToken({ userId: 1, email: 'kakao@demo.app' });
      return Response.redirect(`${baseUrl}/auth/callback?token=${jwt}`);
    }

    const supabase = getServiceSupabase();
    const { data: user, error: dbError } = await supabase
      .from('users')
      .upsert(
        { oauth_provider: 'kakao', oauth_id: kakaoId, email, nickname, profile_image: profileImage },
        { onConflict: 'oauth_provider,oauth_id' },
      )
      .select('id, email')
      .single();

    if (dbError || !user) {
      return Response.redirect(`${baseUrl}/login?error=db_error`);
    }

    const expiresIn = 3600;
    const jwt = createToken({ userId: user.id, email: user.email }, expiresIn);
    const refreshToken = crypto.randomBytes(32).toString('hex');
    const refreshTokenHash = crypto.createHash('sha256').update(refreshToken).digest('hex');

    await supabase.from('refresh_tokens').insert({
      user_id: user.id,
      token_hash: refreshTokenHash,
      expires_at: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString(),
    });

    return Response.redirect(`${baseUrl}/auth/callback?token=${jwt}`);
  } catch {
    return Response.redirect(`${baseUrl}/login?error=kakao_error`);
  }
}
