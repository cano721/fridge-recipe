import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, getUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';

const DEFAULT_PREFS = {
  notifications: { expiryAlert: true, recipeRecommendation: true, marketing: false },
  defaultStorage: 'fridge',
};

export async function GET(request: NextRequest) {
  try {
    if (!isSupabaseConfigured) return successResponse(DEFAULT_PREFS);

    const userId = getUserId(request);
    if (!userId) return successResponse(DEFAULT_PREFS);

    const supabase = getServiceSupabase();
    const { data } = await supabase
      .from('users')
      .select('dietary_prefs')
      .eq('id', userId)
      .single();

    const prefs = (data?.dietary_prefs as Record<string, unknown>) || {};
    return successResponse({ ...DEFAULT_PREFS, ...prefs });
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}

export async function PUT(request: NextRequest) {
  try {
    const body = await request.json();

    if (!isSupabaseConfigured) return successResponse({ message: '설정이 저장되었습니다.' });

    const userId = requireUserId(request);
    const supabase = getServiceSupabase();

    // Merge new prefs into existing
    const { data: existing } = await supabase
      .from('users')
      .select('dietary_prefs')
      .eq('id', userId)
      .single();

    const currentPrefs = (existing?.dietary_prefs as Record<string, unknown>) || {};
    const merged = { ...currentPrefs, ...body };

    const { error } = await supabase
      .from('users')
      .update({ dietary_prefs: merged, updated_at: new Date().toISOString() })
      .eq('id', userId);

    if (error) return errorResponse('DB_ERROR', error.message, 500);
    return successResponse({ message: '설정이 저장되었습니다.' });
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
