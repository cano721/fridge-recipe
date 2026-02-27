import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { getUserId, successResponse, errorResponse } from '@/lib/auth';
import { mockCookingHistory, mockRecipes } from '@/lib/mock-data';

export async function GET(request: NextRequest) {
  try {
    const userId = getUserId(request);
    if (!userId) return errorResponse('UNAUTHORIZED', '로그인이 필요합니다.', 401);

    if (!isSupabaseConfigured) {
      return successResponse(mockCookingHistory);
    }

    const supabase = getServiceSupabase();
    const { data, error } = await supabase
      .from('user_cooking_history')
      .select(`
        id, cooked_at, rating, memo,
        recipes(id, title, thumbnail_url)
      `)
      .eq('user_id', userId)
      .order('cooked_at', { ascending: false })
      .limit(50);

    if (error) return errorResponse('DB_ERROR', error.message, 500);

    const history = (data || []).map((h) => {
      const recipe = h.recipes as unknown as { id: number; title: string; thumbnail_url: string | null } | null;
      return {
        id: h.id,
        recipeId: recipe?.id || h.id,
        recipeTitle: recipe?.title || '삭제된 레시피',
        recipeThumbnailUrl: recipe?.thumbnail_url || null,
        cookedAt: h.cooked_at,
        rating: h.rating,
        memo: h.memo,
      };
    });

    return successResponse(history);
  } catch (e) {
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}

export async function POST(request: NextRequest) {
  try {
    const userId = getUserId(request);
    if (!userId) return errorResponse('UNAUTHORIZED', '로그인이 필요합니다.', 401);

    const body = await request.json();
    const { recipeId, rating, memo } = body;

    if (!recipeId) return errorResponse('VALIDATION_FAILED', 'recipeId가 필요합니다.');

    if (!isSupabaseConfigured) {
      const recipe = mockRecipes.find((r) => r.id === recipeId);
      const newEntry = {
        id: mockCookingHistory.length + 1,
        recipeId,
        recipeTitle: recipe?.title || '레시피',
        recipeThumbnailUrl: recipe?.thumbnailUrl || null,
        cookedAt: new Date().toISOString(),
        rating: rating || null,
        memo: memo || null,
      };
      mockCookingHistory.unshift(newEntry);
      return successResponse(newEntry, 201);
    }

    const supabase = getServiceSupabase();
    const { data, error } = await supabase
      .from('user_cooking_history')
      .insert({
        user_id: userId,
        recipe_id: recipeId,
        rating: rating || null,
        memo: memo || null,
      })
      .select()
      .single();

    if (error) return errorResponse('DB_ERROR', error.message, 500);

    return successResponse(data, 201);
  } catch (e) {
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
