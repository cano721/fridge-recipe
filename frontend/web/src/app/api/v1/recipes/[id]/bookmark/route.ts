import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';

export async function POST(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    if (!isSupabaseConfigured) return successResponse({ message: '북마크가 추가되었습니다.' }, 201);

    const userId = requireUserId(request);
    const { id: idStr } = await params;
    const recipeId = Number(idStr);
    if (!recipeId) return errorResponse('VALIDATION_FAILED', '유효한 레시피 ID가 필요합니다.');

    const supabase = getServiceSupabase();
    const { error } = await supabase
      .from('bookmarks').upsert({ user_id: userId, recipe_id: recipeId }, { onConflict: 'user_id,recipe_id' });

    if (error) return errorResponse('DB_ERROR', error.message, 500);
    return successResponse({ message: '북마크가 추가되었습니다.' }, 201);
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}

export async function DELETE(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    if (!isSupabaseConfigured) return successResponse({ message: '북마크가 삭제되었습니다.' });

    const userId = requireUserId(request);
    const { id: idStr } = await params;
    const recipeId = Number(idStr);
    if (!recipeId) return errorResponse('VALIDATION_FAILED', '유효한 레시피 ID가 필요합니다.');

    const supabase = getServiceSupabase();
    const { error } = await supabase
      .from('bookmarks').delete().eq('user_id', userId).eq('recipe_id', recipeId);

    if (error) return errorResponse('DB_ERROR', error.message, 500);
    return successResponse({ message: '북마크가 삭제되었습니다.' });
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
