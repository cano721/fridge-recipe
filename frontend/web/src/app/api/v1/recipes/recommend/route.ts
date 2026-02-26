import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';
import { mockRecipes, mockUserIngredients } from '@/lib/mock-data';

export async function GET(request: NextRequest) {
  try {
    if (!isSupabaseConfigured) {
      const userIngIds = mockUserIngredients.map((i) => i.ingredientId);
      const recommendations = mockRecipes.map((r) => {
        const total = r.ingredients.length;
        const matched = r.ingredients.filter((i) => userIngIds.includes(i.id)).length;
        return {
          ...r, matchRate: total > 0 ? Math.round((matched / total) * 100) : 0,
          matchedIngredients: matched, missingIngredients: total - matched, totalIngredients: total,
        };
      }).sort((a, b) => b.matchRate - a.matchRate);
      return successResponse(recommendations);
    }

    const userId = requireUserId(request);
    const { searchParams } = new URL(request.url);
    const limit = Number(searchParams.get('limit')) || 10;
    const supabase = getServiceSupabase();

    const { data: userIngredients } = await supabase
      .from('user_ingredients').select('ingredient_id').eq('user_id', userId);
    const userIngredientIds = (userIngredients || []).map((i) => i.ingredient_id);

    if (userIngredientIds.length === 0) return successResponse([]);

    const { data: matchingRecipeIds } = await supabase
      .from('recipe_ingredients').select('recipe_id').in('ingredient_id', userIngredientIds);
    const recipeIds = [...new Set((matchingRecipeIds || []).map((r) => r.recipe_id))];

    if (recipeIds.length === 0) return successResponse([]);

    const { data: recipes, error } = await supabase
      .from('recipes')
      .select(`
        id, title, description, cuisine_type, difficulty, cooking_time, servings,
        calories, thumbnail_url, tags, avg_rating, view_count,
        recipe_ingredients(ingredient_id, is_essential)
      `)
      .in('id', recipeIds).limit(limit);

    if (error) return errorResponse('DB_ERROR', error.message, 500);

    const recommendations = (recipes || []).map((recipe: Record<string, unknown>) => {
      const recipeIngr = recipe.recipe_ingredients as Array<Record<string, unknown>>;
      const total = recipeIngr.length;
      const matched = recipeIngr.filter((ri) => userIngredientIds.includes(ri.ingredient_id as number)).length;
      return {
        id: recipe.id, title: recipe.title, description: recipe.description,
        cuisineType: recipe.cuisine_type, difficulty: recipe.difficulty,
        cookingTime: recipe.cooking_time, servings: recipe.servings, calories: recipe.calories,
        thumbnailUrl: recipe.thumbnail_url, tags: recipe.tags,
        avgRating: recipe.avg_rating, viewCount: recipe.view_count,
        matchRate: total > 0 ? Math.round((matched / total) * 100) : 0,
        matchedIngredients: matched, missingIngredients: total - matched, totalIngredients: total,
      };
    }).sort((a, b) => b.matchRate - a.matchRate);

    return successResponse(recommendations);
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
