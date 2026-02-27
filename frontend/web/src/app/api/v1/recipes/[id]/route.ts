import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { getUserId, successResponse, errorResponse } from '@/lib/auth';
import { mockRecipes, mockUserIngredients } from '@/lib/mock-data';

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

      const userIngIds = mockUserIngredients.map((i) => i.ingredientId);
      const ingredients = recipe.ingredients.map((ing) => ({
        ...ing,
        hasIngredient: userIngIds.includes(ing.ingredientId),
      }));

      return successResponse({
        id: recipe.id, title: recipe.title, description: recipe.description,
        cuisineType: recipe.cuisineType, difficulty: recipe.difficulty,
        cookingTime: recipe.cookingTime, servings: recipe.servings,
        thumbnailUrl: recipe.thumbnailUrl, tags: recipe.tags,
        avgRating: recipe.avgRating, viewCount: recipe.viewCount,
        steps: recipe.steps, nutrition: recipe.nutrition,
        ingredients, isBookmarked: false,
      });
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
    let userIngredientIds: number[] = [];
    if (userId) {
      const [bookmarkRes, ingredientsRes] = await Promise.all([
        supabase.from('bookmarks').select('user_id')
          .eq('user_id', userId).eq('recipe_id', id).maybeSingle(),
        supabase.from('user_ingredients').select('ingredient_id')
          .eq('user_id', userId),
      ]);
      isBookmarked = !!bookmarkRes.data;
      userIngredientIds = (ingredientsRes.data || []).map((i) => i.ingredient_id);
    }

    await supabase.from('recipes').update({ view_count: (recipe.view_count || 0) + 1 }).eq('id', id);

    const ingredients = (recipe.recipe_ingredients as Array<Record<string, unknown>>).map((ri) => {
      const master = ri.ingredient_master as unknown as Record<string, unknown>;
      return {
        ingredientId: master.id,
        name: master.name,
        quantity: ri.quantity,
        isEssential: ri.is_essential,
        hasIngredient: userIngredientIds.includes(master.id as number),
      };
    });

    // Build nutrition from recipe fields
    const nutritionData = recipe.nutrition as Record<string, number> | null;
    const nutrition = nutritionData ? {
      calories: nutritionData.calories ?? recipe.calories ?? 0,
      protein: nutritionData.protein ?? 0,
      fat: nutritionData.fat ?? 0,
      carbs: nutritionData.carbs ?? 0,
    } : (recipe.calories ? { calories: recipe.calories as number, protein: 0, fat: 0, carbs: 0 } : null);

    return successResponse({
      id: recipe.id, title: recipe.title, description: recipe.description,
      cuisineType: recipe.cuisine_type, difficulty: recipe.difficulty,
      cookingTime: recipe.cooking_time, servings: recipe.servings,
      thumbnailUrl: recipe.thumbnail_url,
      steps: Array.isArray(recipe.steps) ? (recipe.steps as Array<Record<string, unknown>>).map((s) => ({
        order: s.order ?? s.step,
        description: s.description,
        imageUrl: s.imageUrl || s.image_url || undefined,
        timerSeconds: s.timerSeconds || s.timer_seconds || undefined,
      })) : [],
      nutrition,
      tags: recipe.tags, avgRating: recipe.avg_rating, viewCount: recipe.view_count,
      ingredients, isBookmarked,
    });
  } catch (e) {
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
