import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { getUserId, successResponse, errorResponse } from '@/lib/auth';
import { mockUserLikes, mockRecipeLikes } from '@/lib/mock-data';

export async function POST(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id: idStr } = await params;
    const id = Number(idStr);
    if (!id) return errorResponse('VALIDATION_FAILED', '유효한 ID가 필요합니다.');

    const userId = getUserId(request);
    if (!userId) return errorResponse('UNAUTHORIZED', '로그인이 필요합니다.', 401);

    if (!isSupabaseConfigured) {
      if (!mockUserLikes.includes(id)) {
        mockUserLikes.push(id);
        mockRecipeLikes[id] = (mockRecipeLikes[id] || 0) + 1;
      }
      return successResponse({ liked: true, likeCount: mockRecipeLikes[id] || 0 });
    }

    const supabase = getServiceSupabase();
    await supabase
      .from('recipe_likes')
      .upsert({ user_id: userId, recipe_id: id }, { onConflict: 'user_id,recipe_id', ignoreDuplicates: true });

    const { count } = await supabase
      .from('recipe_likes')
      .select('*', { count: 'exact', head: true })
      .eq('recipe_id', id);

    return successResponse({ liked: true, likeCount: count || 0 });
  } catch (e) {
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}

export async function DELETE(
  request: NextRequest,
  { params }: { params: Promise<{ id: string }> }
) {
  try {
    const { id: idStr } = await params;
    const id = Number(idStr);
    if (!id) return errorResponse('VALIDATION_FAILED', '유효한 ID가 필요합니다.');

    const userId = getUserId(request);
    if (!userId) return errorResponse('UNAUTHORIZED', '로그인이 필요합니다.', 401);

    if (!isSupabaseConfigured) {
      const idx = mockUserLikes.indexOf(id);
      if (idx !== -1) {
        mockUserLikes.splice(idx, 1);
        mockRecipeLikes[id] = Math.max(0, (mockRecipeLikes[id] || 0) - 1);
      }
      return successResponse({ liked: false, likeCount: mockRecipeLikes[id] || 0 });
    }

    const supabase = getServiceSupabase();
    await supabase
      .from('recipe_likes')
      .delete()
      .eq('user_id', userId)
      .eq('recipe_id', id);

    const { count } = await supabase
      .from('recipe_likes')
      .select('*', { count: 'exact', head: true })
      .eq('recipe_id', id);

    return successResponse({ liked: false, likeCount: count || 0 });
  } catch (e) {
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
