import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';
import { mockRecipes, mockUserIngredients } from '@/lib/mock-data';

function getMatchLabel(ratio: number): string {
  if (ratio >= 1) return 'perfect';
  if (ratio >= 0.7) return 'great';
  if (ratio >= 0.4) return 'good';
  return 'partial';
}

export async function GET(request: NextRequest) {
  try {
    if (!isSupabaseConfigured) {
      const userIngIds = mockUserIngredients.map((i) => i.ingredientId);
      const recommendations = mockRecipes.map((r) => {
        const matched = r.ingredients.filter((i) => userIngIds.includes(i.ingredientId));
        const missing = r.ingredients.filter((i) => !userIngIds.includes(i.ingredientId));
        const matchRatio = r.ingredients.length > 0 ? matched.length / r.ingredients.length : 0;
        return {
          recipe: {
            id: r.id, title: r.title, description: r.description,
            cuisineType: r.cuisineType, difficulty: r.difficulty,
            cookingTime: r.cookingTime, servings: r.servings,
            thumbnailUrl: r.thumbnailUrl, tags: r.tags,
            avgRating: r.avgRating, viewCount: r.viewCount,
          },
          matchRatio,
          matchedIngredients: matched.map((i) => i.name),
          missingIngredients: missing.map((i) => i.name),
          matchLabel: getMatchLabel(matchRatio),
        };
      }).sort((a, b) => b.matchRatio - a.matchRatio);
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
        thumbnail_url, tags, avg_rating, view_count,
        recipe_ingredients(ingredient_id, is_essential,
          ingredient_master(id, name))
      `)
      .in('id', recipeIds).limit(limit);

    if (error) return errorResponse('DB_ERROR', error.message, 500);

    const recommendations = (recipes || []).map((recipe: Record<string, unknown>) => {
      const recipeIngr = recipe.recipe_ingredients as Array<Record<string, unknown>>;
      const matched: string[] = [];
      const missing: string[] = [];

      for (const ri of recipeIngr) {
        const master = ri.ingredient_master as unknown as Record<string, unknown>;
        const name = master?.name as string || 'unknown';
        if (userIngredientIds.includes(ri.ingredient_id as number)) {
          matched.push(name);
        } else {
          missing.push(name);
        }
      }

      const total = recipeIngr.length;
      const matchRatio = total > 0 ? matched.length / total : 0;

      return {
        recipe: {
          id: recipe.id, title: recipe.title, description: recipe.description,
          cuisineType: recipe.cuisine_type, difficulty: recipe.difficulty,
          cookingTime: recipe.cooking_time, servings: recipe.servings,
          thumbnailUrl: recipe.thumbnail_url, tags: recipe.tags,
          avgRating: recipe.avg_rating, viewCount: recipe.view_count,
        },
        matchRatio,
        matchedIngredients: matched,
        missingIngredients: missing,
        matchLabel: getMatchLabel(matchRatio),
      };
    }).sort((a, b) => b.matchRatio - a.matchRatio);

    return successResponse(recommendations);
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
