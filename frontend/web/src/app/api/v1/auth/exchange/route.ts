import { NextRequest } from 'next/server';
import { exchangeAuthCode } from '@/lib/auth-code-store';
import { errorResponse, successResponse } from '@/lib/auth';

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();
    const { code } = body;

    if (!code || typeof code !== 'string') {
      return errorResponse('INVALID_CODE', '유효하지 않은 코드입니다.', 400);
    }

    const tokens = exchangeAuthCode(code);
    if (!tokens) {
      return errorResponse('CODE_EXPIRED', '코드가 만료되었거나 유효하지 않습니다.', 400);
    }

    return successResponse({
      accessToken: tokens.accessToken,
      refreshToken: tokens.refreshToken,
      expiresIn: 3600,
    });
  } catch {
    return errorResponse('EXCHANGE_FAILED', '토큰 교환에 실패했습니다.', 500);
  }
}
