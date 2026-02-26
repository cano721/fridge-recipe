import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';
import { mockUserIngredients } from '@/lib/mock-data';

export async function GET(request: NextRequest) {
  try {
    const { searchParams } = new URL(request.url);
    const days = Number(searchParams.get('days')) || 3;

    if (!isSupabaseConfigured) {
      const now = new Date();
      const target = new Date();
      target.setDate(now.getDate() + days);
      const expiring = mockUserIngredients.filter((i) => {
        if (!i.expiryDate) return false;
        const exp = new Date(i.expiryDate);
        return exp <= target;
      });
      return successResponse(expiring);
    }

    const userId = requireUserId(request);
    const supabase = getServiceSupabase();
    const targetDate = new Date();
    targetDate.setDate(targetDate.getDate() + days);

    const { data, error } = await supabase
      .from('user_ingredients')
      .select(`
        id, quantity, unit, expiry_date, storage_type, memo, registered_via, created_at,
        ingredient_master!inner(id, name, category, icon_url, default_unit)
      `)
      .eq('user_id', userId)
      .not('expiry_date', 'is', null)
      .lte('expiry_date', targetDate.toISOString().split('T')[0])
      .order('expiry_date', { ascending: true });

    if (error) return errorResponse('DB_ERROR', error.message, 500);

    const items = (data || []).map((item: Record<string, unknown>) => {
      const master = item.ingredient_master as unknown as Record<string, unknown>;
      return {
        id: item.id, ingredientId: master.id, ingredientName: master.name,
        category: master.category, iconUrl: master.icon_url,
        quantity: item.quantity, unit: item.unit || master.default_unit,
        expiryDate: item.expiry_date, storageType: item.storage_type,
        memo: item.memo, registeredVia: item.registered_via, createdAt: item.created_at,
      };
    });

    return successResponse(items);
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
