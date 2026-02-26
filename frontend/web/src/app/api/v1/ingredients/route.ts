import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';
import { mockUserIngredients } from '@/lib/mock-data';

export async function GET(request: NextRequest) {
  try {
    // Mock mode
    if (!isSupabaseConfigured) {
      const { searchParams } = new URL(request.url);
      const storageType = searchParams.get('storageType');
      const data = storageType
        ? mockUserIngredients.filter((i) => i.storageType === storageType)
        : mockUserIngredients;
      return successResponse(data);
    }

    const userId = requireUserId(request);
    const { searchParams } = new URL(request.url);
    const page = Number(searchParams.get('page')) || 1;
    const size = Number(searchParams.get('size')) || 20;
    const storageType = searchParams.get('storageType');

    const supabase = getServiceSupabase();
    const from = (page - 1) * size;
    const to = from + size - 1;

    let query = supabase
      .from('user_ingredients')
      .select(`
        id, quantity, unit, expiry_date, storage_type, memo, registered_via, created_at,
        ingredient_master!inner(id, name, category, icon_url, default_unit)
      `, { count: 'exact' })
      .eq('user_id', userId)
      .order('created_at', { ascending: false })
      .range(from, to);

    if (storageType) query = query.eq('storage_type', storageType);

    const { data, error } = await query;
    if (error) return errorResponse('DB_ERROR', error.message, 500);

    const ingredients = (data || []).map((item: Record<string, unknown>) => {
      const master = item.ingredient_master as unknown as Record<string, unknown>;
      return {
        id: item.id, ingredientId: master.id, ingredientName: master.name,
        category: master.category, iconUrl: master.icon_url,
        quantity: item.quantity, unit: item.unit || master.default_unit,
        expiryDate: item.expiry_date, storageType: item.storage_type,
        memo: item.memo, registeredVia: item.registered_via, createdAt: item.created_at,
      };
    });

    return successResponse(ingredients);
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}

export async function POST(request: NextRequest) {
  try {
    const body = await request.json();

    if (!isSupabaseConfigured) {
      return successResponse({ id: Date.now(), ...body, registeredVia: 'manual', createdAt: new Date().toISOString() }, 201);
    }

    const userId = requireUserId(request);
    const { ingredientId, quantity, unit, expiryDate, storageType = 'fridge', memo } = body;
    if (!ingredientId) return errorResponse('VALIDATION_FAILED', 'ingredientId는 필수입니다.');

    const supabase = getServiceSupabase();
    const { data, error } = await supabase
      .from('user_ingredients')
      .upsert({
        user_id: userId, ingredient_id: ingredientId, quantity, unit,
        expiry_date: expiryDate || null, storage_type: storageType, memo, registered_via: 'manual',
      }, { onConflict: 'user_id,ingredient_id,storage_type' })
      .select(`
        id, quantity, unit, expiry_date, storage_type, memo, registered_via, created_at,
        ingredient_master!inner(id, name, category, icon_url, default_unit)
      `)
      .single();

    if (error) return errorResponse('DB_ERROR', error.message, 500);

    const master = data.ingredient_master as unknown as Record<string, unknown>;
    return successResponse({
      id: data.id, ingredientId: master.id, ingredientName: master.name,
      category: master.category, iconUrl: master.icon_url,
      quantity: data.quantity, unit: data.unit || master.default_unit,
      expiryDate: data.expiry_date, storageType: data.storage_type,
      memo: data.memo, registeredVia: data.registered_via, createdAt: data.created_at,
    }, 201);
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
