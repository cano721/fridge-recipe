import { NextRequest } from 'next/server';
import crypto from 'crypto';

let _jwtSecret: string | null = null;

function getJwtSecret(): string {
  if (_jwtSecret) return _jwtSecret;
  const secret = process.env.JWT_SECRET;
  if (!secret && process.env.NODE_ENV === 'production') {
    throw new Error('JWT_SECRET must be set in production');
  }
  if (!secret) {
    console.warn('[auth] JWT_SECRET not set — using dev fallback. DO NOT use in production.');
  }
  _jwtSecret = secret || 'dev-secret-key-change-in-production';
  return _jwtSecret;
}

function hmacSign(data: string): string {
  return crypto.createHmac('sha256', getJwtSecret()).update(data).digest('base64url');
}

export function createToken(payload: Record<string, unknown>, expiresIn = 3600): string {
  const header = { alg: 'HS256', typ: 'JWT' };
  const now = Math.floor(Date.now() / 1000);
  const claims = { ...payload, iat: now, exp: now + expiresIn };

  const headerB64 = Buffer.from(JSON.stringify(header)).toString('base64url');
  const payloadB64 = Buffer.from(JSON.stringify(claims)).toString('base64url');
  const signature = hmacSign(`${headerB64}.${payloadB64}`);

  return `${headerB64}.${payloadB64}.${signature}`;
}

export function verifyToken(token: string): Record<string, unknown> | null {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) return null;

    const expectedSig = hmacSign(`${parts[0]}.${parts[1]}`);

    const sigBuf = Buffer.from(parts[2], 'base64url');
    const expectedBuf = Buffer.from(expectedSig, 'base64url');
    if (sigBuf.length !== expectedBuf.length || !crypto.timingSafeEqual(sigBuf, expectedBuf)) {
      return null;
    }

    const header = JSON.parse(Buffer.from(parts[0], 'base64url').toString());
    if (header.alg !== 'HS256' || header.typ !== 'JWT') return null;

    const payload = JSON.parse(Buffer.from(parts[1], 'base64url').toString());
    if (payload.exp && payload.exp < Math.floor(Date.now() / 1000)) return null;

    return payload;
  } catch {
    return null;
  }
}

export function getUserId(request: NextRequest): number | null {
  const authHeader = request.headers.get('Authorization');
  if (!authHeader?.startsWith('Bearer ')) return null;

  const token = authHeader.slice(7);
  const payload = verifyToken(token);
  return payload?.userId as number | null;
}

export function requireUserId(request: NextRequest): number {
  const userId = getUserId(request);
  if (!userId) throw new AuthError('인증이 필요합니다.');
  return userId;
}

export class AuthError extends Error {
  constructor(message: string) {
    super(message);
    this.name = 'AuthError';
  }
}

const REFRESH_TOKEN_COOKIE_OPTIONS = {
  httpOnly: true,
  secure: process.env.NODE_ENV === 'production',
  sameSite: 'strict' as const,
  path: '/api/v1/auth',
  maxAge: 30 * 24 * 60 * 60, // 30 days
};

export { REFRESH_TOKEN_COOKIE_OPTIONS };

export function jsonResponse(data: unknown, status = 200) {
  return Response.json(data, { status });
}

export function successResponse(data: unknown, status = 200) {
  return jsonResponse({ success: true, data }, status);
}

export function errorResponse(code: string, message: string, status = 400) {
  return jsonResponse({ success: false, error: { code, message } }, status);
}
