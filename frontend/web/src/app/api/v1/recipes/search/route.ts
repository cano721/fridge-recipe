import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { successResponse, errorResponse } from '@/lib/auth';
import { mockRecipes } from '@/lib/mock-data';

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const query = searchParams.get('q') || '';
    const cuisineType = searchParams.get('cuisineType');

    if (!query.trim()) return successResponse([]);

    if (!isSupabaseConfigured) {
      let results = mockRecipes.filter((r) =>
        r.title.includes(query) || r.description.includes(query) ||
        r.tags.some((t) => t.includes(query))
      );
      if (cuisineType) results = results.filter((r) => r.cuisineType === cuisineType);
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
      .range(from, to)
      .order('avg_rating', { ascending: false });

    if (cuisineType) dbQuery = dbQuery.eq('cuisine_type', cuisineType);

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
