import { NextRequest } from 'next/server';
import { getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';

export async function PUT(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const userId = requireUserId(request);
    const { id: idStr } = await params;
    const id = Number(idStr);
    if (!id) return errorResponse('VALIDATION_FAILED', '유효한 ID가 필요합니다.');

    const body = await request.json();
    const updates: Record<string, unknown> = { updated_at: new Date().toISOString() };
    if (body.quantity !== undefined) updates.quantity = body.quantity;
    if (body.unit !== undefined) updates.unit = body.unit;
    if (body.expiryDate !== undefined) updates.expiry_date = body.expiryDate;
    if (body.storageType !== undefined) updates.storage_type = body.storageType;
    if (body.memo !== undefined) updates.memo = body.memo;

    const supabase = getServiceSupabase();
    const { data, error } = await supabase
      .from('user_ingredients')
      .update(updates)
      .eq('id', id)
      .eq('user_id', userId)
      .select(`
        id, quantity, unit, expiry_date, storage_type, memo, registered_via, created_at,
        ingredient_master!inner(id, name, category, icon_url, default_unit)
      `)
      .single();

    if (error) return errorResponse('NOT_FOUND', '식재료를 찾을 수 없습니다.', 404);

    const master = data.ingredient_master as unknown as Record<string, unknown>;
    return successResponse({
      id: data.id,
      ingredientId: master.id,
      ingredientName: master.name,
      category: master.category,
      iconUrl: master.icon_url,
      quantity: data.quantity,
      unit: data.unit || master.default_unit,
      expiryDate: data.expiry_date,
      storageType: data.storage_type,
      memo: data.memo,
      registeredVia: data.registered_via,
      createdAt: data.created_at,
    });
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}

export async function DELETE(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const userId = requireUserId(request);
    const { id: idStr } = await params;
    const id = Number(idStr);
    if (!id) return errorResponse('VALIDATION_FAILED', '유효한 ID가 필요합니다.');

    const supabase = getServiceSupabase();
    const { error } = await supabase
      .from('user_ingredients')
      .delete()
      .eq('id', id)
      .eq('user_id', userId);

    if (error) return errorResponse('DB_ERROR', error.message, 500);

    return successResponse({ message: '삭제되었습니다.' });
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
