import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { successResponse, errorResponse } from '@/lib/auth';
import { mockCategories } from '@/lib/mock-data';

export async function GET() {
  try {
    if (!isSupabaseConfigured) return successResponse(mockCategories);

    const supabase = getServiceSupabase();
    const { data, error } = await supabase
      .from('ingredient_master')
      .select('category')
      .order('category');

    if (error) return errorResponse('DB_ERROR', error.message, 500);
    const categories = [...new Set((data || []).map((d) => d.category))];
    return successResponse(categories);
  } catch (e) {
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
