import { createClient, SupabaseClient } from '@supabase/supabase-js';

const supabaseUrl = process.env.NEXT_PUBLIC_SUPABASE_URL || '';
const supabaseAnonKey = process.env.NEXT_PUBLIC_SUPABASE_ANON_KEY || '';
const supabaseServiceKey = process.env.SUPABASE_SERVICE_ROLE_KEY || '';

export const isSupabaseConfigured = !!(supabaseUrl && supabaseAnonKey && supabaseServiceKey);

// Client-side (anon key, respects RLS)
export const supabase: SupabaseClient | null = isSupabaseConfigured
  ? createClient(supabaseUrl, supabaseAnonKey)
  : null;

// Server-side (service role, bypasses RLS) - only use in API routes
export function getServiceSupabase(): SupabaseClient {
  if (!isSupabaseConfigured) throw new Error('SUPABASE_NOT_CONFIGURED');
  return createClient(supabaseUrl, supabaseServiceKey);
}
