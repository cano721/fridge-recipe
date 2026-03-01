import { NextRequest, NextResponse } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { createToken } from '@/lib/auth';
import { storeAuthCode } from '@/lib/auth-code-store';
import crypto from 'crypto';

const GOOGLE_CLIENT_ID = process.env.GOOGLE_CLIENT_ID || '';
const GOOGLE_CLIENT_SECRET = process.env.GOOGLE_CLIENT_SECRET || '';

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
  const state = searchParams.get('state');

  // CSRF: state 검증
  const savedState = request.cookies.get('oauth_state')?.value;
  if (!state || !savedState || state !== savedState) {
    return Response.redirect(`${baseUrl}/login?error=invalid_state`);
  }

  if (error || !code) {
    return Response.redirect(`${baseUrl}/login?error=google_denied`);
  }

  // state 쿠키 제거
  const clearStateCookie = (res: NextResponse) => {
    res.cookies.set('oauth_state', '', { maxAge: 0, path: '/' });
    return res;
  };

  try {
    // Exchange code for access token
    const redirectUri = `${baseUrl}/api/v1/auth/google/callback`;
    const tokenRes = await fetch('https://oauth2.googleapis.com/token', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        code,
        client_id: GOOGLE_CLIENT_ID,
        client_secret: GOOGLE_CLIENT_SECRET,
        redirect_uri: redirectUri,
        grant_type: 'authorization_code',
      }),
    });

    if (!tokenRes.ok) {
      return Response.redirect(`${baseUrl}/login?error=google_token_failed`);
    }

    const tokenData = await tokenRes.json();

    // Get user profile
    const profileRes = await fetch('https://www.googleapis.com/oauth2/v3/userinfo', {
      headers: { Authorization: `Bearer ${tokenData.access_token}` },
    });

    if (!profileRes.ok) {
      return Response.redirect(`${baseUrl}/login?error=google_profile_failed`);
    }

    const profile = await profileRes.json();
    const googleId = profile.sub;
    const nickname = profile.name || 'Google 사용자';
    const email = profile.email || null;
    const profileImage = profile.picture || null;

    // Upsert user
    if (!isSupabaseConfigured) {
      const jwt = createToken({ userId: 1, email: 'google@demo.app' });
      const authCode = storeAuthCode(jwt, '');
      const res = NextResponse.redirect(`${baseUrl}/auth/callback?code=${authCode}`);
      return clearStateCookie(res);
    }

    const supabase = getServiceSupabase();
    const { data: user, error: dbError } = await supabase
      .from('users')
      .upsert(
        { oauth_provider: 'google', oauth_id: googleId, email, nickname, profile_image: profileImage },
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

    const authCode = storeAuthCode(jwt, refreshToken);
    const res = NextResponse.redirect(`${baseUrl}/auth/callback?code=${authCode}`);
    return clearStateCookie(res);
  } catch {
    return Response.redirect(`${baseUrl}/login?error=google_error`);
  }
}
