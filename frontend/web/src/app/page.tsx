'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/api';
import { useAuth } from '@/lib/auth-context';
import { Ingredient, RecipeRecommendation } from '@/types';
import { getCategoryEmoji, getStorageLabel, formatCookingTime } from '@/lib/utils';
import ExpiryBadge from '@/components/ui/ExpiryBadge';
import { SkeletonListItem, SkeletonCard } from '@/components/ui/LoadingSpinner';

const quickActions = [
  { label: '식재료 추가', emoji: '🥦', href: '/fridge' },
  { label: '영수증 스캔', emoji: '📷', href: '/scan' },
  { label: '레시피 추천', emoji: '🍳', href: '/recipe' },
];

export default function HomePage() {
  const router = useRouter();
  const { isLoggedIn, isLoading: authLoading, user } = useAuth();
  const [expiringItems, setExpiringItems] = useState<Ingredient[]>([]);
  const [recommendations, setRecommendations] = useState<RecipeRecommendation[]>([]);
  const [loadingExpiring, setLoadingExpiring] = useState(true);
  const [loadingRecipes, setLoadingRecipes] = useState(true);

  useEffect(() => {
    if (authLoading) return;
    if (!isLoggedIn) {
      setLoadingExpiring(false);
      setLoadingRecipes(false);
      return;
    }

    const fetchExpiring = async () => {
      try {
        const res = await api.getExpiringItems(7);
        if (res?.data) setExpiringItems(res.data);
      } catch {
        // silent
      } finally {
        setLoadingExpiring(false);
      }
    };

    const fetchRecipes = async () => {
      try {
        const res = await api.getRecommendations(4);
        if (res?.data) setRecommendations(res.data);
      } catch {
        // silent
      } finally {
        setLoadingRecipes(false);
      }
    };

    fetchExpiring();
    fetchRecipes();
  }, [isLoggedIn, authLoading]);

  return (
    <div className="min-h-screen bg-surface-variant">
      {/* Header */}
      <header className="bg-gradient-to-r from-primary-700 to-primary text-white px-6 pt-12 pb-8 rounded-b-[28px]">
        <h1 className="text-2xl font-semibold">냉장고 레시피</h1>
        <p className="text-white/80 text-sm mt-1">
          {isLoggedIn ? `${user?.nickname || '요리사'}님, 오늘 뭐 먹을까요?` : '오늘 뭐 먹을까요?'}
        </p>
      </header>

      <div className="px-4 py-5 space-y-6">
        {/* Quick Actions */}
        <section>
          <div className="grid grid-cols-3 gap-3">
            {quickActions.map((action) => (
              <button
                key={action.href}
                onClick={() => router.push(action.href)}
                className="bg-surface rounded-2xl p-4 flex flex-col items-center gap-2 active:scale-[0.97] transition-transform"
                style={{ boxShadow: 'var(--shadow-level-1)', transitionDuration: 'var(--duration-fast)' }}
              >
                <span className="text-3xl">{action.emoji}</span>
                <span className="text-xs font-medium text-on-surface text-center leading-tight">
                  {action.label}
                </span>
              </button>
            ))}
          </div>
        </section>

        {/* Login Prompt (when not logged in) */}
        {!authLoading && !isLoggedIn && (
          <section
            className="bg-surface rounded-2xl p-5"
            style={{ boxShadow: 'var(--shadow-level-1)' }}
          >
            <div className="text-center">
              <p className="text-3xl mb-3">👋</p>
              <p className="text-sm font-semibold text-on-surface mb-1">로그인하고 맞춤 추천 받기</p>
              <p className="text-xs text-on-surface-variant mb-4">
                냉장고 속 재료를 등록하면 딱 맞는 레시피를 추천해드려요
              </p>
              <button
                onClick={() => router.push('/login')}
                className="h-10 px-6 bg-primary text-white rounded-xl text-sm font-medium active:scale-[0.97] transition-transform"
                style={{ transitionDuration: 'var(--duration-fast)' }}
              >
                시작하기
              </button>
            </div>
          </section>
        )}

        {/* Expiring Items (logged in only) */}
        {isLoggedIn && (
          <section>
            <h2 className="text-base font-bold text-on-surface mb-3 flex items-center gap-2">
              <span>⚠️</span> 소비기한 임박
            </h2>
            {loadingExpiring ? (
              <div className="space-y-2">
                {[1, 2, 3].map((i) => (
                  <SkeletonListItem key={i} />
                ))}
              </div>
            ) : expiringItems.length === 0 ? (
              <div
                className="bg-surface rounded-2xl p-5 text-center text-on-surface-variant text-sm"
                style={{ boxShadow: 'var(--shadow-level-1)' }}
              >
                소비기한 임박 식재료가 없습니다
              </div>
            ) : (
              <div className="space-y-2">
                {expiringItems.map((item) => (
                  <div
                    key={item.id}
                    className="bg-surface rounded-2xl p-4 flex items-center justify-between"
                    style={{ boxShadow: 'var(--shadow-level-1)' }}
                  >
                    <div className="flex items-center gap-3">
                      <span className="text-2xl">{getCategoryEmoji(item.category)}</span>
                      <div>
                        <p className="text-sm font-semibold text-on-surface">{item.name}</p>
                        <p className="text-xs text-on-surface-variant">{getStorageLabel(item.storageType)}</p>
                      </div>
                    </div>
                    <ExpiryBadge daysUntilExpiry={item.daysUntilExpiry} />
                  </div>
                ))}
              </div>
            )}
          </section>
        )}

        {/* Recommended Recipes */}
        <section>
          <h2 className="text-base font-bold text-on-surface mb-3 flex items-center gap-2">
            <span>✨</span> {isLoggedIn ? '추천 레시피' : '인기 레시피'}
          </h2>
          {loadingRecipes ? (
            <div className="grid grid-cols-2 gap-3">
              {[1, 2, 3, 4].map((i) => (
                <SkeletonCard key={i} />
              ))}
            </div>
          ) : !isLoggedIn || recommendations.length === 0 ? (
            <div
              className="bg-surface rounded-2xl p-5 text-center text-on-surface-variant text-sm"
              style={{ boxShadow: 'var(--shadow-level-1)' }}
            >
              {isLoggedIn ? '냉장고 재료를 추가하면 레시피를 추천해드려요' : '로그인하면 맞춤 레시피를 추천해드려요'}
            </div>
          ) : (
            <div className="grid grid-cols-2 gap-3">
              {recommendations.map((rec) => (
                <button
                  key={rec.recipe.id}
                  onClick={() => router.push(`/recipe/${rec.recipe.id}`)}
                  className="bg-surface rounded-[20px] p-4 text-left active:scale-[0.97] transition-transform"
                  style={{ boxShadow: 'var(--shadow-level-1)', transitionDuration: 'var(--duration-fast)' }}
                >
                  <p className="text-sm font-semibold text-on-surface line-clamp-2 leading-tight mb-2">
                    {rec.recipe.title}
                  </p>
                  {rec.recipe.cuisineType && (
                    <span className="inline-block text-xs bg-primary/10 text-primary rounded-lg px-2 py-0.5 mb-2">
                      {rec.recipe.cuisineType}
                    </span>
                  )}
                  {rec.recipe.cookingTime && (
                    <p className="text-xs text-on-surface-variant mb-2">
                      ⏱ {formatCookingTime(rec.recipe.cookingTime)}
                    </p>
                  )}
                  <div className="mt-1">
                    <div className="flex justify-between items-center mb-1">
                      <span className="text-xs text-on-surface-variant">재료 보유</span>
                      <span className="text-xs font-semibold text-primary">
                        {Math.round(rec.matchRatio * 100)}%
                      </span>
                    </div>
                    <div className="h-1.5 bg-outline-variant rounded-full overflow-hidden">
                      <div
                        className="h-full bg-primary rounded-full transition-all"
                        style={{ width: `${Math.round(rec.matchRatio * 100)}%`, transitionDuration: 'var(--duration-medium)' }}
                      />
                    </div>
                  </div>
                </button>
              ))}
            </div>
          )}
        </section>
      </div>
    </div>
  );
}
