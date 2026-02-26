import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { successResponse, errorResponse } from '@/lib/auth';
import { mockIngredientMaster } from '@/lib/mock-data';

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const query = searchParams.get('q') || '';
    if (!query) return successResponse([]);

    if (!isSupabaseConfigured) {
      const results = mockIngredientMaster.filter((i) => i.name.includes(query));
      return successResponse(results);
    }

    const supabase = getServiceSupabase();
    const { data, error } = await supabase
      .from('ingredient_master')
      .select('id, name, category, icon_url, default_unit')
      .ilike('name', `%${query}%`)
      .limit(20);

    if (error) return errorResponse('DB_ERROR', error.message, 500);

    return successResponse(
      (data || []).map((item) => ({
        id: item.id, name: item.name, category: item.category,
        iconUrl: item.icon_url, defaultUnit: item.default_unit,
      }))
    );
  } catch (e) {
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
