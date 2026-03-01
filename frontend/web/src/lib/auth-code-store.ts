import crypto from 'crypto';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';

const CODE_TTL_SECONDS = 60;

// --- In-memory fallback (dev/mock mode) ---
interface StoredAuth {
  accessToken: string;
  refreshToken: string;
  expiresAt: number;
}
const memoryStore = new Map<string, StoredAuth>();

function memoryCleanup() {
  const now = Date.now();
  for (const [code, entry] of memoryStore) {
    if (now > entry.expiresAt) memoryStore.delete(code);
  }
}

// --- Public API ---

export async function storeAuthCode(accessToken: string, refreshToken: string): Promise<string> {
  const code = crypto.randomBytes(32).toString('hex');

  if (isSupabaseConfigured) {
    const supabase = getServiceSupabase();
    await supabase.from('auth_codes').insert({
      code,
      access_token: accessToken,
      refresh_token: refreshToken,
      expires_at: new Date(Date.now() + CODE_TTL_SECONDS * 1000).toISOString(),
    });
  } else {
    memoryCleanup();
    memoryStore.set(code, {
      accessToken,
      refreshToken,
      expiresAt: Date.now() + CODE_TTL_SECONDS * 1000,
    });
  }

  return code;
}

export async function exchangeAuthCode(code: string): Promise<{ accessToken: string; refreshToken: string } | null> {
  if (isSupabaseConfigured) {
    const supabase = getServiceSupabase();

    // 원자적 조회 + 삭제: 먼저 조회 후 즉시 삭제 (1회용)
    const { data, error } = await supabase
      .from('auth_codes')
      .select('access_token, refresh_token, expires_at')
      .eq('code', code)
      .single();

    if (error || !data) return null;

    // 즉시 삭제 (재사용 방지)
    await supabase.from('auth_codes').delete().eq('code', code);

    if (new Date(data.expires_at) < new Date()) return null;

    return { accessToken: data.access_token, refreshToken: data.refresh_token };
  }

  // In-memory fallback
  memoryCleanup();
  const entry = memoryStore.get(code);
  if (!entry) return null;
  memoryStore.delete(code);
  if (Date.now() > entry.expiresAt) return null;
  return { accessToken: entry.accessToken, refreshToken: entry.refreshToken };
}
