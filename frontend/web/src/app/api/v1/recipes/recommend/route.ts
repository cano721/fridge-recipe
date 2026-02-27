import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';
import { mockRecipes, mockUserIngredients } from '@/lib/mock-data';

const AI_SERVICE_URL = process.env.AI_SERVICE_URL;
const INTERNAL_API_KEY = process.env.INTERNAL_API_KEY || 'dev-internal-key';

function getMatchLabel(ratio: number): string {
  if (ratio >= 1) return 'perfect';
  if (ratio >= 0.7) return 'great';
  if (ratio >= 0.4) return 'good';
  return 'partial';
}

interface AIRecommendation {
  recipe_id: number;
  total_score: number;
  match_ratio: number;
  matched_count: number;
  total_required: number;
  missing_ingredients: number[];
  match_label: string;
}

async function callAIService(
  recipes: Record<string, unknown>[],
  userIngredients: Record<string, unknown>[],
  limit: number,
): Promise<AIRecommendation[] | null> {
  if (!AI_SERVICE_URL) return null;

  const body = {
    recipes: recipes.map((r) => {
      const ingredients = r.recipe_ingredients as Array<Record<string, unknown>>;
      return {
        id: r.id,
        title: r.title,
        ingredients: ingredients.map((ri) => {
          const master = ri.ingredient_master as Record<string, unknown>;
          return {
            id: master?.id ?? ri.ingredient_id,
            name: master?.name ?? 'unknown',
            is_essential: ri.is_essential ?? true,
            substitute_ids: [],
          };
        }),
        view_count: r.view_count ?? 0,
        avg_rating: r.avg_rating ?? 0,
      };
    }),
    user_ingredients: userIngredients.map((ui) => ({
      ingredient_id: ui.ingredient_id,
      expiry_date: ui.expiry_date ?? null,
      storage_type: ui.storage_type ?? 'fridge',
    })),
    limit,
  };

  try {
    const res = await fetch(`${AI_SERVICE_URL}/ai/recommend`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-Internal-Api-Key': INTERNAL_API_KEY,
      },
      body: JSON.stringify(body),
      signal: AbortSignal.timeout(5000),
    });
    if (!res.ok) return null;
    const data = await res.json();
    return data.recommendations ?? null;
  } catch {
    return null;
  }
}

function simpleRecommend(
  recipes: Record<string, unknown>[],
  userIngredientIds: number[],
) {
  return recipes.map((recipe) => {
    const recipeIngr = recipe.recipe_ingredients as Array<Record<string, unknown>>;
    const matched: string[] = [];
    const missing: string[] = [];

    for (const ri of recipeIngr) {
      const master = ri.ingredient_master as Record<string, unknown>;
      const name = (master?.name as string) || 'unknown';
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

    // Fetch user ingredients with expiry and storage info
    const { data: userIngredients } = await supabase
      .from('user_ingredients')
      .select('ingredient_id, expiry_date, storage_type')
      .eq('user_id', userId);

    const userIngList = userIngredients || [];
    const userIngredientIds = userIngList.map((i) => i.ingredient_id);

    if (userIngredientIds.length === 0) return successResponse([]);

    // Find recipes that use at least 1 of user's ingredients
    const { data: matchingRecipeIds } = await supabase
      .from('recipe_ingredients').select('recipe_id').in('ingredient_id', userIngredientIds);
    const recipeIds = [...new Set((matchingRecipeIds || []).map((r) => r.recipe_id))];

    if (recipeIds.length === 0) return successResponse([]);

    // Fetch full recipe data with ingredients
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

    // Try AI service for smart recommendations
    const aiResults = await callAIService(
      recipes as unknown as Record<string, unknown>[],
      userIngList as unknown as Record<string, unknown>[],
      limit,
    );

    if (aiResults && aiResults.length > 0) {
      // Build lookup maps
      const recipeMap = new Map<number, Record<string, unknown>>();
      for (const r of recipes) {
        recipeMap.set(r.id as number, r as unknown as Record<string, unknown>);
      }

      const ingredientNameMap = new Map<number, string>();
      for (const r of recipes) {
        const ingrs = (r as unknown as Record<string, unknown>).recipe_ingredients as Array<Record<string, unknown>>;
        for (const ri of ingrs) {
          const master = ri.ingredient_master as Record<string, unknown>;
          if (master?.id && master?.name) {
            ingredientNameMap.set(master.id as number, master.name as string);
          }
        }
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
            matchLabel: getMatchLabel(ai.match_ratio),
          };
        })
        .filter(Boolean);

      return successResponse(recommendations);
    }

    // Fallback: simple ratio-based matching
    const results = simpleRecommend(
      recipes as unknown as Record<string, unknown>[],
      userIngredientIds,
    ).slice(0, limit);

    return successResponse(results);
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
