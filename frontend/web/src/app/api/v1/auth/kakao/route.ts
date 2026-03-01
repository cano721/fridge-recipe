import { NextRequest, NextResponse } from 'next/server';
import crypto from 'crypto';

const KAKAO_CLIENT_ID = process.env.KAKAO_CLIENT_ID || '';

function getBaseUrl(request: NextRequest) {
  if (process.env.NEXT_PUBLIC_APP_URL) return process.env.NEXT_PUBLIC_APP_URL;
  const host = request.headers.get('host') || 'localhost:3000';
  const proto = request.headers.get('x-forwarded-proto') || 'http';
  return `${proto}://${host}`;
}

export async function GET(request: NextRequest) {
  if (!KAKAO_CLIENT_ID) {
    const baseUrl = getBaseUrl(request);
    return Response.redirect(`${baseUrl}/login?error=kakao_not_configured`);
  }

  const baseUrl = getBaseUrl(request);
  const redirectUri = `${baseUrl}/api/v1/auth/kakao/callback`;

  const state = crypto.randomBytes(32).toString('hex');

  const params = new URLSearchParams({
    client_id: KAKAO_CLIENT_ID,
    redirect_uri: redirectUri,
    response_type: 'code',
    scope: 'profile_nickname,account_email',
    state,
  });

  const response = NextResponse.redirect(`https://kauth.kakao.com/oauth/authorize?${params}`);
  response.cookies.set('oauth_state', state, {
    httpOnly: true,
    secure: process.env.NODE_ENV === 'production',
    sameSite: 'lax',
    maxAge: 300,
    path: '/',
  });

  return response;
}
