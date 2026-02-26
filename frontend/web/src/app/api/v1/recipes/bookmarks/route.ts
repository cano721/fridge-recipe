import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';
import { mockRecipes } from '@/lib/mock-data';

export async function GET(request: NextRequest) {
  try {
    if (!isSupabaseConfigured) {
      // Mock: 처음 2개 레시피를 북마크로 반환
      const bookmarked = mockRecipes.slice(0, 2);
      return Response.json({ success: true, data: bookmarked, meta: { page: 1, size: 20, total: bookmarked.length } });
    }

    const userId = requireUserId(request);
    const { searchParams } = new URL(request.url);
    const page = Number(searchParams.get('page')) || 1;
    const size = Number(searchParams.get('size')) || 20;
    const from = (page - 1) * size;
    const to = from + size - 1;

    const supabase = getServiceSupabase();
    const { data, count, error } = await supabase
      .from('bookmarks')
      .select(`created_at, recipes!inner(id, title, description, cuisine_type, difficulty, cooking_time, servings, calories, thumbnail_url, tags, avg_rating, view_count)`, { count: 'exact' })
      .eq('user_id', userId)
      .order('created_at', { ascending: false })
      .range(from, to);

    if (error) return errorResponse('DB_ERROR', error.message, 500);

    const recipes = (data || []).map((b: Record<string, unknown>) => {
      const r = b.recipes as unknown as Record<string, unknown>;
      return {
        id: r.id, title: r.title, description: r.description, cuisineType: r.cuisine_type,
        difficulty: r.difficulty, cookingTime: r.cooking_time, servings: r.servings,
        calories: r.calories, thumbnailUrl: r.thumbnail_url, tags: r.tags,
        avgRating: r.avg_rating, viewCount: r.view_count, bookmarkedAt: b.created_at,
      };
    });

    return Response.json({ success: true, data: recipes, meta: { page, size, total: count || 0 } });
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
