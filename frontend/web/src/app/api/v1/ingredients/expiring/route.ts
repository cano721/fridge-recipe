import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';
import { mockUserIngredients } from '@/lib/mock-data';

function computeDaysUntilExpiry(expiryDate: string | null): number | null {
  if (!expiryDate) return null;
  return Math.ceil((new Date(expiryDate).getTime() - Date.now()) / 86400000);
}

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
      const expiryDate = item.expiry_date as string | null;
      return {
        id: item.id,
        ingredientId: master.id,
        name: master.name,
        category: master.category,
        quantity: item.quantity,
        unit: item.unit || master.default_unit,
        expiryDate,
        storageType: item.storage_type,
        memo: item.memo,
        registeredVia: item.registered_via,
        daysUntilExpiry: computeDaysUntilExpiry(expiryDate),
      };
    });

    return successResponse(items);
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
