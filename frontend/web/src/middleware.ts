import { NextRequest, NextResponse } from 'next/server';

const RATE_LIMIT_WINDOW_MS = 60_000; // 1 minute
const RATE_LIMIT_MAX = 20; // max requests per window per IP

const rateLimitStore = new Map<string, { count: number; resetAt: number }>();

function cleanupRateLimit() {
  const now = Date.now();
  for (const [key, entry] of rateLimitStore) {
    if (now > entry.resetAt) rateLimitStore.delete(key);
  }
}

function isRateLimited(ip: string): boolean {
  const now = Date.now();
  const entry = rateLimitStore.get(ip);

  if (!entry || now > entry.resetAt) {
    rateLimitStore.set(ip, { count: 1, resetAt: now + RATE_LIMIT_WINDOW_MS });
    return false;
  }

  entry.count++;
  return entry.count > RATE_LIMIT_MAX;
}

export function middleware(request: NextRequest) {
  if (!request.nextUrl.pathname.startsWith('/api/v1/auth/')) {
    return NextResponse.next();
  }

  // Periodic cleanup
  if (Math.random() < 0.01) cleanupRateLimit();

  const ip = request.headers.get('x-forwarded-for')?.split(',')[0]?.trim()
    || request.headers.get('x-real-ip')
    || '127.0.0.1';

  if (isRateLimited(ip)) {
    return NextResponse.json(
      { success: false, error: { code: 'RATE_LIMITED', message: '요청이 너무 많습니다. 잠시 후 다시 시도해주세요.' } },
      { status: 429 },
    );
  }

  return NextResponse.next();
}

export const config = {
  matcher: '/api/v1/auth/:path*',
};
