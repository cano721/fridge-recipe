import { NextRequest } from 'next/server';

const KAKAO_CLIENT_ID = process.env.KAKAO_CLIENT_ID || '';

function getBaseUrl(request: NextRequest) {
  const host = request.headers.get('host') || 'localhost:3000';
  const proto = request.headers.get('x-forwarded-proto') || 'http';
  return `${proto}://${host}`;
}

export async function GET(request: NextRequest) {
  if (!KAKAO_CLIENT_ID) {
    return Response.json(
      { success: false, error: { code: 'NOT_CONFIGURED', message: '카카오 로그인이 설정되지 않았습니다.' } },
      { status: 503 },
    );
  }

  const baseUrl = getBaseUrl(request);
  const redirectUri = `${baseUrl}/api/v1/auth/kakao/callback`;

  const params = new URLSearchParams({
    client_id: KAKAO_CLIENT_ID,
    redirect_uri: redirectUri,
    response_type: 'code',
    scope: 'profile_nickname,account_email',
  });

  return Response.redirect(`https://kauth.kakao.com/oauth/authorize?${params}`);
}
