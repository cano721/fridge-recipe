import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { getUserId, successResponse, errorResponse } from '@/lib/auth';
import { mockRecipes } from '@/lib/mock-data';

export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id: idStr } = await params;
    const id = Number(idStr);
    if (!id) return errorResponse('VALIDATION_FAILED', '유효한 ID가 필요합니다.');

    if (!isSupabaseConfigured) {
      const recipe = mockRecipes.find((r) => r.id === id);
      if (!recipe) return errorResponse('NOT_FOUND', '레시피를 찾을 수 없습니다.', 404);
      return successResponse({ ...recipe, isBookmarked: false, sourceUrl: null, sourceType: 'manual', createdAt: new Date().toISOString() });
    }

    const userId = getUserId(request);
    const supabase = getServiceSupabase();

    const { data: recipe, error } = await supabase
      .from('recipes')
      .select(`
        id, title, description, cuisine_type, difficulty, cooking_time, servings,
        calories, thumbnail_url, steps, nutrition, tags, avg_rating, view_count,
        source_url, source_type, created_at,
        recipe_ingredients(id, quantity, is_essential, substitute_ids,
          ingredient_master(id, name, category, icon_url, default_unit))
      `)
      .eq('id', id).single();

    if (error) return errorResponse('NOT_FOUND', '레시피를 찾을 수 없습니다.', 404);

    let isBookmarked = false;
    if (userId) {
      const { data: bookmark } = await supabase
        .from('bookmarks').select('user_id')
        .eq('user_id', userId).eq('recipe_id', id).maybeSingle();
      isBookmarked = !!bookmark;
    }

    await supabase.from('recipes').update({ view_count: (recipe.view_count || 0) + 1 }).eq('id', id);

    const ingredients = (recipe.recipe_ingredients as Array<Record<string, unknown>>).map((ri) => {
      const master = ri.ingredient_master as unknown as Record<string, unknown>;
      return { id: master.id, name: master.name, category: master.category, iconUrl: master.icon_url, quantity: ri.quantity, isEssential: ri.is_essential, substituteIds: ri.substitute_ids };
    });

    return successResponse({
      id: recipe.id, title: recipe.title, description: recipe.description,
      cuisineType: recipe.cuisine_type, difficulty: recipe.difficulty,
      cookingTime: recipe.cooking_time, servings: recipe.servings, calories: recipe.calories,
      thumbnailUrl: recipe.thumbnail_url, steps: recipe.steps, nutrition: recipe.nutrition,
      tags: recipe.tags, avgRating: recipe.avg_rating, viewCount: recipe.view_count,
      sourceUrl: recipe.source_url, sourceType: recipe.source_type, createdAt: recipe.created_at,
      ingredients, isBookmarked,
    });
  } catch (e) {
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
