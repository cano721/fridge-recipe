import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';

export async function PUT(request: NextRequest) {
  try {
    const { dietaryPrefs } = await request.json();
    if (!dietaryPrefs) return errorResponse('VALIDATION_FAILED', 'dietaryPrefs는 필수입니다.');

    if (!isSupabaseConfigured) return successResponse({ message: '선호도가 업데이트되었습니다.' });

    const userId = requireUserId(request);
    const supabase = getServiceSupabase();
    const { error } = await supabase
      .from('users')
      .update({ dietary_prefs: dietaryPrefs, updated_at: new Date().toISOString() })
      .eq('id', userId);

    if (error) return errorResponse('DB_ERROR', error.message, 500);
    return successResponse({ message: '선호도가 업데이트되었습니다.' });
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
