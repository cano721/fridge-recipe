import crypto from 'crypto';

interface StoredAuth {
  accessToken: string;
  refreshToken: string;
  expiresAt: number;
}

const store = new Map<string, StoredAuth>();

const CODE_TTL_MS = 60_000; // 60 seconds

export function storeAuthCode(accessToken: string, refreshToken: string): string {
  cleanup();
  const code = crypto.randomBytes(32).toString('hex');
  store.set(code, {
    accessToken,
    refreshToken,
    expiresAt: Date.now() + CODE_TTL_MS,
  });
  return code;
}

export function exchangeAuthCode(code: string): { accessToken: string; refreshToken: string } | null {
  cleanup();
  const entry = store.get(code);
  if (!entry) return null;
  store.delete(code);
  if (Date.now() > entry.expiresAt) return null;
  return { accessToken: entry.accessToken, refreshToken: entry.refreshToken };
}

function cleanup() {
  const now = Date.now();
  for (const [code, entry] of store) {
    if (now > entry.expiresAt) store.delete(code);
  }
}
