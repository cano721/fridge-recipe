import { NextRequest, NextResponse } from 'next/server';
import crypto from 'crypto';

const GOOGLE_CLIENT_ID = process.env.GOOGLE_CLIENT_ID || '';

function getBaseUrl(request: NextRequest) {
  if (process.env.NEXT_PUBLIC_APP_URL) return process.env.NEXT_PUBLIC_APP_URL;
  const host = request.headers.get('host') || 'localhost:3000';
  const proto = request.headers.get('x-forwarded-proto') || 'http';
  return `${proto}://${host}`;
}

export async function GET(request: NextRequest) {
  if (!GOOGLE_CLIENT_ID) {
    const baseUrl = getBaseUrl(request);
    return Response.redirect(`${baseUrl}/login?error=google_not_configured`);
  }

  const baseUrl = getBaseUrl(request);
  const redirectUri = `${baseUrl}/api/v1/auth/google/callback`;

  const state = crypto.randomBytes(32).toString('hex');

  const params = new URLSearchParams({
    client_id: GOOGLE_CLIENT_ID,
    redirect_uri: redirectUri,
    response_type: 'code',
    scope: 'openid email profile',
    access_type: 'offline',
    prompt: 'consent',
    state,
  });

  const response = NextResponse.redirect(`https://accounts.google.com/o/oauth2/v2/auth?${params}`);
  response.cookies.set('oauth_state', state, {
    httpOnly: true,
    secure: process.env.NODE_ENV === 'production',
    sameSite: 'lax',
    maxAge: 300,
    path: '/',
  });

  return response;
}
