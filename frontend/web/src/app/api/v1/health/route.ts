import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { successResponse } from '@/lib/auth';

export async function GET() {
  if (!isSupabaseConfigured) {
    return successResponse({ status: 'ok', db: 'mock', redis: 'not_applicable', aiService: 'not_applicable' });
  }

  let dbStatus = 'disconnected';
  try {
    const supabase = getServiceSupabase();
    const { error } = await supabase.from('users').select('id').limit(1);
    dbStatus = error ? 'disconnected' : 'connected';
  } catch {
    dbStatus = 'disconnected';
  }

  return successResponse({
    status: dbStatus === 'connected' ? 'ok' : 'degraded',
    db: dbStatus,
    redis: 'not_applicable',
    aiService: 'not_applicable',
  });
}
