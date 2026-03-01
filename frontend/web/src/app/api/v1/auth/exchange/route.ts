import { NextRequest, NextResponse } from 'next/server';
import { exchangeAuthCode } from '@/lib/auth-code-store';
import { errorResponse, REFRESH_TOKEN_COOKIE_OPTIONS } from '@/lib/auth';

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const { code } = body;

    if (!code || typeof code !== 'string') {
      return errorResponse('INVALID_CODE', '유효하지 않은 코드입니다.', 400);
    }

    const tokens = await exchangeAuthCode(code);
    if (!tokens) {
      return errorResponse('CODE_EXPIRED', '코드가 만료되었거나 유효하지 않습니다.', 400);
    }

    const response = NextResponse.json({
      success: true,
      data: { accessToken: tokens.accessToken, expiresIn: 3600 },
    });

    if (tokens.refreshToken) {
      response.cookies.set('refreshToken', tokens.refreshToken, REFRESH_TOKEN_COOKIE_OPTIONS);
    }

    return response;
  } catch {
    return errorResponse('EXCHANGE_FAILED', '토큰 교환에 실패했습니다.', 500);
  }
}
