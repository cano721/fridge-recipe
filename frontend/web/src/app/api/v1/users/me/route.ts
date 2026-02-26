import { NextRequest } from 'next/server';
import { isSupabaseConfigured, getServiceSupabase } from '@/lib/supabase';
import { requireUserId, successResponse, errorResponse, AuthError } from '@/lib/auth';
import { mockUser } from '@/lib/mock-data';

export async function GET(request: NextRequest) {
  try {
    if (!isSupabaseConfigured) return successResponse(mockUser);

    const userId = requireUserId(request);
    const supabase = getServiceSupabase();
    const { data: user, error } = await supabase
      .from('users')
      .select('id, email, nickname, profile_image, oauth_provider, dietary_prefs')
      .eq('id', userId).single();

    if (error || !user) return errorResponse('NOT_FOUND', '사용자를 찾을 수 없습니다.', 404);

    return successResponse({
      id: user.id, email: user.email, nickname: user.nickname,
      profileImage: user.profile_image, oauthProvider: user.oauth_provider, dietaryPrefs: user.dietary_prefs,
    });
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}

export async function PUT(request: NextRequest) {
  try {
    const body = await request.json();
    if (!isSupabaseConfigured) return successResponse({ ...mockUser, ...body });

    const userId = requireUserId(request);
    const updates: Record<string, unknown> = { updated_at: new Date().toISOString() };
    if (body.nickname !== undefined) updates.nickname = body.nickname;
    if (body.profileImage !== undefined) updates.profile_image = body.profileImage;

    const supabase = getServiceSupabase();
    const { data: user, error } = await supabase
      .from('users').update(updates).eq('id', userId)
      .select('id, email, nickname, profile_image, oauth_provider, dietary_prefs').single();

    if (error) return errorResponse('DB_ERROR', error.message, 500);

    return successResponse({
      id: user.id, email: user.email, nickname: user.nickname,
      profileImage: user.profile_image, oauthProvider: user.oauth_provider, dietaryPrefs: user.dietary_prefs,
    });
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}

export async function DELETE(request: NextRequest) {
  try {
    if (!isSupabaseConfigured) return successResponse({ message: '회원 탈퇴가 완료되었습니다.' });

    const userId = requireUserId(request);
    const supabase = getServiceSupabase();
    const { error } = await supabase.from('users').delete().eq('id', userId);
    if (error) return errorResponse('DB_ERROR', error.message, 500);

    return successResponse({ message: '회원 탈퇴가 완료되었습니다.' });
  } catch (e) {
    if (e instanceof AuthError) return errorResponse('UNAUTHORIZED', e.message, 401);
    return errorResponse('INTERNAL_ERROR', (e as Error).message, 500);
  }
}
