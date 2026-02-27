import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';
import { mockRecipes, mockUserIngredients } from '@/lib/mock-data';
import { recommend, type RecipeInput, type UserIngredientInput } from '@/lib/recommendation-engine';

function getMatchLabel(ratio: number): string {
  if (ratio >= 0.80) return '바로 요리 가능';
  if (ratio >= 0.50) return '재료 조금 부족';
  if (ratio >= 0.30) return '도전해보세요';
  return '재료 부족';
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
      .from('user_ingredients')
      .select('ingredient_id, expiry_date, storage_type')
      .eq('user_id', userId);

    const userIngList = userIngredients || [];
    const userIngredientIds = userIngList.map((i) => i.ingredient_id);

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
      .in('id', recipeIds)
      .limit(50);

    if (error) return errorResponse('DB_ERROR', error.message, 500);
    if (!recipes || recipes.length === 0) return successResponse([]);

    // 레시피 데이터를 엔진 입력 형식으로 변환
    const recipeInputs: RecipeInput[] = recipes.map((r) => {
      const ingredients = r.recipe_ingredients as Array<Record<string, unknown>>;
      return {
        id: r.id as number,
        title: r.title as string,
        description: r.description as string | null,
        cuisine_type: r.cuisine_type as string | null,
        difficulty: r.difficulty as string | null,
        cooking_time: r.cooking_time as number | null,
        servings: r.servings as number,
        thumbnail_url: r.thumbnail_url as string | null,
        tags: r.tags as string[],
        avg_rating: (r.avg_rating as number) ?? 0,
        view_count: (r.view_count as number) ?? 0,
        ingredients: ingredients.map((ri) => {
          const master = ri.ingredient_master as Record<string, unknown>;
          return {
            id: (master?.id ?? ri.ingredient_id) as number,
            name: (master?.name as string) ?? 'unknown',
            is_essential: (ri.is_essential as boolean) ?? true,
            substitute_ids: [] as number[],
          };
        }),
      };
    });

    const userIngInputs: UserIngredientInput[] = userIngList.map((ui) => ({
      ingredient_id: ui.ingredient_id,
      expiry_date: ui.expiry_date ?? null,
      storage_type: ui.storage_type ?? 'fridge',
    }));

    // 추천 엔진 실행
    const aiResults = recommend(recipeInputs, userIngInputs, limit);

    // 결과를 응답 형식으로 변환
    const recipeMap = new Map<number, Record<string, unknown>>();
    for (const r of recipes) {
      recipeMap.set(r.id as number, r as unknown as Record<string, unknown>);
    }

    const recommendations = aiResults
      .map((ai) => {
        const recipe = recipeMap.get(ai.recipe_id);
        if (!recipe) return null;

        const recipeIngr = recipe.recipe_ingredients as Array<Record<string, unknown>>;
        const matched: string[] = [];
        const missing: string[] = [];

        for (const ri of recipeIngr) {
          const master = ri.ingredient_master as Record<string, unknown>;
          const name = (master?.name as string) || 'unknown';
          const ingId = master?.id as number;
          if (ai.missing_ingredients.includes(ingId)) {
            missing.push(name);
          } else {
            matched.push(name);
          }
        }

        return {
          recipe: {
            id: recipe.id, title: recipe.title, description: recipe.description,
            cuisineType: recipe.cuisine_type, difficulty: recipe.difficulty,
            cookingTime: recipe.cooking_time, servings: recipe.servings,
            thumbnailUrl: recipe.thumbnail_url, tags: recipe.tags,
            avgRating: recipe.avg_rating, viewCount: recipe.view_count,
          },
          matchRatio: ai.match_ratio,
          matchedIngredients: matched,
          missingIngredients: missing,
          matchLabel: ai.match_label,
          totalScore: ai.total_score,
          expiryScore: ai.expiry_score,
        };
      })
      .filter(Boolean);

    return successResponse(recommendations);
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
