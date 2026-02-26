import { NextRequest } from 'next/server';

const JWT_SECRET = process.env.JWT_SECRET || 'dev-secret-key-change-in-production';

// Simple JWT implementation for API routes (no external deps)
export function createToken(payload: Record<string, unknown>, expiresIn = 3600): string {
  const header = { alg: 'HS256', typ: 'JWT' };
  const now = Math.floor(Date.now() / 1000);
  const claims = { ...payload, iat: now, exp: now + expiresIn };

  const enc = (obj: unknown) => btoa(JSON.stringify(obj)).replace(/=/g, '').replace(/\+/g, '-').replace(/\//g, '_');
  const headerB64 = enc(header);
  const payloadB64 = enc(claims);

  // For production, use proper HMAC-SHA256. This is a simple demo implementation.
  const signature = btoa(`${headerB64}.${payloadB64}.${JWT_SECRET}`)
    .replace(/=/g, '').replace(/\+/g, '-').replace(/\//g, '_');

  return `${headerB64}.${payloadB64}.${signature}`;
}

export function verifyToken(token: string): Record<string, unknown> | null {
  try {
    const parts = token.split('.');
    if (parts.length !== 3) return null;

    const expectedSig = btoa(`${parts[0]}.${parts[1]}.${JWT_SECRET}`)
      .replace(/=/g, '').replace(/\+/g, '-').replace(/\//g, '_');

    if (parts[2] !== expectedSig) return null;

    const payload = JSON.parse(atob(parts[1].replace(/-/g, '+').replace(/_/g, '/')));
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

export function jsonResponse(data: unknown, status = 200) {
  return Response.json(data, { status });
}

export function successResponse(data: unknown, status = 200) {
  return jsonResponse({ success: true, data }, status);
}

export function errorResponse(code: string, message: string, status = 400) {
  return jsonResponse({ success: false, error: { code, message } }, status);
}
