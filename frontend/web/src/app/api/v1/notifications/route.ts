import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';
import { mockUserIngredients } from '@/lib/mock-data';

interface Notification {
  id: string;
  type: 'expiry_urgent' | 'expiry_warning' | 'expiry_notice';
  title: string;
  message: string;
  ingredientName: string;
  daysUntilExpiry: number;
  createdAt: string;
}

function buildNotifications(
  items: Array<{ id: number; name: string; daysUntilExpiry: number; storageType: string }>,
): Notification[] {
  const now = new Date().toISOString();
  return items
    .filter((item) => item.daysUntilExpiry <= 7)
    .map((item) => {
      let type: Notification['type'];
      let title: string;

      if (item.daysUntilExpiry <= 0) {
        type = 'expiry_urgent';
        title = '소비기한이 지났어요!';
      } else if (item.daysUntilExpiry <= 2) {
        type = 'expiry_urgent';
        title = '오늘 내로 사용하세요!';
      } else if (item.daysUntilExpiry <= 4) {
        type = 'expiry_warning';
        title = '소비기한이 임박해요';
      } else {
        type = 'expiry_notice';
        title = '소비기한을 확인하세요';
      }

      const daysText =
        item.daysUntilExpiry <= 0
          ? '소비기한이 지났습니다'
          : item.daysUntilExpiry === 1
            ? '내일까지 사용해주세요'
            : `${item.daysUntilExpiry}일 남았습니다`;

      return {
        id: `expiry-${item.id}`,
        type,
        title,
        message: `${item.name}: ${daysText}`,
        ingredientName: item.name,
        daysUntilExpiry: item.daysUntilExpiry,
        createdAt: now,
      };
    })
    .sort((a, b) => a.daysUntilExpiry - b.daysUntilExpiry);
}

export async function GET(request: NextRequest) {
  try {
    if (!isSupabaseConfigured) {
      const items = mockUserIngredients.map((i) => ({
        id: i.id,
        name: i.name,
        daysUntilExpiry: i.daysUntilExpiry ?? 99,
        storageType: i.storageType,
      }));
      return successResponse(buildNotifications(items));
    }

    const userId = requireUserId(request);
    const supabase = getServiceSupabase();

    const { data, error } = await supabase
      .from('user_ingredients')
      .select('id, expiry_date, storage_type, ingredient_master(name)')
      .eq('user_id', userId)
      .not('expiry_date', 'is', null);

    if (error) return errorResponse('DB_ERROR', error.message, 500);

    const today = new Date();
    today.setHours(0, 0, 0, 0);

    const items = (data || []).map((row: Record<string, unknown>) => {
      const master = row.ingredient_master as Record<string, unknown> | null;
      const expiry = new Date(row.expiry_date as string);
      expiry.setHours(0, 0, 0, 0);
      const diffDays = Math.ceil((expiry.getTime() - today.getTime()) / (1000 * 60 * 60 * 24));
      return {
        id: row.id as number,
        name: (master?.name as string) || '알 수 없음',
        daysUntilExpiry: diffDays,
        storageType: (row.storage_type as string) || 'fridge',
      };
    });

    return successResponse(buildNotifications(items));
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
