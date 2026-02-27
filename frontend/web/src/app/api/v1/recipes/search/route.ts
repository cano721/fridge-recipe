import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { successResponse, errorResponse } from '@/lib/auth';
import { mockRecipes } from '@/lib/mock-data';

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const query = searchParams.get('q') || '';
    const cuisineType = searchParams.get('cuisineType');
    const difficulty = searchParams.get('difficulty');
    const maxCookingTime = searchParams.get('maxCookingTime') ? Number(searchParams.get('maxCookingTime')) : null;
    const ingredientIdsRaw = searchParams.get('ingredientIds');
    const ingredientIds = ingredientIdsRaw
      ? ingredientIdsRaw.split(',').map(Number).filter(Boolean)
      : null;
    const sort = searchParams.get('sort');

    if (!query.trim()) return successResponse([]);

    if (!isSupabaseConfigured) {
      let results = mockRecipes.filter((r) =>
        r.title.includes(query) || r.description.includes(query) ||
        r.tags.some((t) => t.includes(query))
      );
      if (cuisineType) results = results.filter((r) => r.cuisineType === cuisineType);
      if (difficulty) results = results.filter((r) => r.difficulty === difficulty);
      if (maxCookingTime) results = results.filter((r) => r.cookingTime <= maxCookingTime);
      if (ingredientIds && ingredientIds.length > 0) {
        results = results.filter((r) =>
          (r as unknown as { ingredients?: { id: number }[] }).ingredients?.some((ing) =>
            ingredientIds.includes(ing.id)
          )
        );
      }
      if (sort === 'rating') {
        results = [...results].sort((a, b) => (b.avgRating ?? 0) - (a.avgRating ?? 0));
      } else if (sort === 'cookingTime') {
        results = [...results].sort((a, b) => (a.cookingTime ?? 0) - (b.cookingTime ?? 0));
      } else if (sort === 'newest') {
        results = [...results].sort((a, b) => b.id - a.id);
      }
      return Response.json({ success: true, data: results, meta: { page: 1, size: 20, total: results.length } });
    }

    const page = Number(searchParams.get('page')) || 1;
    const size = Number(searchParams.get('size')) || 20;
    const from = (page - 1) * size;
    const to = from + size - 1;

    const supabase = getServiceSupabase();
    let dbQuery = supabase
      .from('recipes')
      .select('id, title, description, cuisine_type, difficulty, cooking_time, servings, calories, thumbnail_url, tags, avg_rating, view_count', { count: 'exact' })
      .ilike('title', `%${query}%`)
      .range(from, to);

    if (cuisineType) dbQuery = dbQuery.eq('cuisine_type', cuisineType);
    if (difficulty) dbQuery = dbQuery.eq('difficulty', difficulty);
    if (maxCookingTime) dbQuery = dbQuery.lte('cooking_time', maxCookingTime);
    if (ingredientIds && ingredientIds.length > 0) {
      dbQuery = dbQuery.in('ingredients.id', ingredientIds);
    }

    if (sort === 'rating') {
      dbQuery = dbQuery.order('avg_rating', { ascending: false });
    } else if (sort === 'cookingTime') {
      dbQuery = dbQuery.order('cooking_time', { ascending: true });
    } else if (sort === 'newest') {
      dbQuery = dbQuery.order('id', { ascending: false });
    } else {
      dbQuery = dbQuery.order('avg_rating', { ascending: false });
    }

    const { data, count, error } = await dbQuery;
    if (error) return errorResponse('DB_ERROR', error.message, 500);

    const recipes = (data || []).map((r) => ({
      id: r.id, title: r.title, description: r.description, cuisineType: r.cuisine_type,
      difficulty: r.difficulty, cookingTime: r.cooking_time, servings: r.servings,
      calories: r.calories, thumbnailUrl: r.thumbnail_url, tags: r.tags,
      avgRating: r.avg_rating, viewCount: r.view_count,
    }));

    return Response.json({ success: true, data: recipes, meta: { page, size, total: count || 0 } });
  } catch (e) {
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
