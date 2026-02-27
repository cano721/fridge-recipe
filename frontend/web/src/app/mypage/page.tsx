'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/api';
import { useAuth } from '@/lib/auth-context';
import {
  User,
  Bell,
  UserCog,
  Bookmark,
  Clock,
  ChefHat,
  ChevronRight,
  LogOut,
} from 'lucide-react';
import EmptyState from '@/components/ui/EmptyState';

interface UserStats {
  ingredientCount: number;
  savedRecipeCount: number;
  weekCookCount: number;
}

const menuItems = [
  { label: '프로필 수정', icon: UserCog, href: '/mypage/edit' },
  { label: '내 북마크', icon: Bookmark, href: '/mypage/bookmarks' },
  { label: '조리 이력', icon: Clock, href: '/mypage/history' },
  { label: '알림 설정', icon: Bell, href: '/settings' },
] as const;

export default function MyPage() {
  const router = useRouter();
  const { isLoggedIn, isLoading: authLoading, user, logout } = useAuth();
  const [stats, setStats] = useState<UserStats>({
    ingredientCount: 0,
    savedRecipeCount: 0,
    weekCookCount: 0,
  });

  useEffect(() => {
    if (!isLoggedIn) return;

    const fetchStats = async () => {
      try {
        const [ingredientsRes, bookmarksRes, historyRes] = await Promise.all([
          api.getIngredients(),
          api.getBookmarks(),
          api.getCookingHistory(),
        ]);

        const history = historyRes?.data || [];
        const weekAgo = new Date();
        weekAgo.setDate(weekAgo.getDate() - 7);
        const weekCookCount = history.filter(
          (h: { cookedAt: string }) => new Date(h.cookedAt) >= weekAgo
        ).length;

        setStats({
          ingredientCount: ingredientsRes?.data?.length ?? 0,
          savedRecipeCount: bookmarksRes?.data?.length ?? 0,
          weekCookCount,
        });
      } catch {
        // silent
      }
    };
    fetchStats();
  }, [isLoggedIn]);

  const handleLogout = () => {
    logout();
    router.push('/login');
  };

  if (!authLoading && !isLoggedIn) {
    return (
      <div className="min-h-screen bg-surface-variant">
        <header className="bg-surface px-6 pt-12 pb-6" style={{ boxShadow: 'var(--shadow-level-1)' }}>
          <h1 className="text-[22px] font-semibold text-on-surface">마이페이지</h1>
        </header>
        <div className="px-4 py-8">
          <EmptyState
            icon={<span className="text-6xl">🔒</span>}
            title="로그인이 필요해요"
            description="마이페이지를 이용하려면 로그인해주세요"
            ctaLabel="로그인하기"
            onCtaClick={() => router.push('/login')}
          />
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-surface-variant">
      <header className="bg-surface px-6 pt-12 pb-6" style={{ boxShadow: 'var(--shadow-level-1)' }}>
        <h1 className="text-[22px] font-semibold text-on-surface">마이페이지</h1>
      </header>

      <div className="px-4 py-4 space-y-4">
        {/* Profile Section */}
        <section
          className="bg-surface rounded-2xl p-6 flex flex-col items-center gap-3"
          style={{ boxShadow: 'var(--shadow-level-1)' }}
        >
          <div className="w-20 h-20 rounded-full bg-primary-100 flex items-center justify-center">
            <User className="w-10 h-10 text-primary" />
          </div>
          <div className="text-center">
            <p className="text-base font-bold text-on-surface">{user?.nickname || '게스트'}</p>
            <p className="text-sm text-on-surface-variant mt-0.5">{user?.email || ''}</p>
          </div>
        </section>

        {/* Stats Row */}
        <section
          className="bg-surface rounded-2xl"
          style={{ boxShadow: 'var(--shadow-level-1)' }}
        >
          <div className="grid grid-cols-3 divide-x divide-outline-variant">
            <div className="flex flex-col items-center py-5">
              <span className="text-2xl font-bold text-primary">{stats.ingredientCount}</span>
              <span className="text-xs text-on-surface-variant mt-1">내 식재료</span>
            </div>
            <div className="flex flex-col items-center py-5">
              <span className="text-2xl font-bold text-primary">{stats.savedRecipeCount}</span>
              <span className="text-xs text-on-surface-variant mt-1">저장 레시피</span>
            </div>
            <div className="flex flex-col items-center py-5">
              <span className="text-2xl font-bold text-primary">{stats.weekCookCount}</span>
              <span className="text-xs text-on-surface-variant mt-1">이번 주 조리</span>
            </div>
          </div>
        </section>

        {/* Menu List */}
        <section
          className="bg-surface rounded-2xl overflow-hidden"
          style={{ boxShadow: 'var(--shadow-level-1)' }}
        >
          {menuItems.map((item, index) => {
            const Icon = item.icon;
            return (
              <button
                key={item.label}
                onClick={() => router.push(item.href)}
                className={`w-full flex items-center gap-3 px-5 py-4 active:bg-primary-50 transition-colors ${
                  index < menuItems.length - 1 ? 'border-b border-outline-variant' : ''
                }`}
                style={{ transitionDuration: 'var(--duration-fast)' }}
              >
                <Icon className="w-5 h-5 text-on-surface-variant flex-shrink-0" />
                <span className="flex-1 text-left text-sm font-medium text-on-surface">
                  {item.label}
                </span>
                <ChevronRight className="w-4 h-4 text-on-surface-variant" />
              </button>
            );
          })}
        </section>

        {/* Logout */}
        <button
          onClick={handleLogout}
          className="w-full flex items-center justify-center gap-2 py-4 text-danger text-sm font-medium active:opacity-70 transition-opacity"
        >
          <LogOut className="w-4 h-4" />
          로그아웃
        </button>
      </div>
    </div>
  );
}
