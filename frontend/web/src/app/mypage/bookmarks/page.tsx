'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { ArrowLeft, Clock } from 'lucide-react';
import { api } from '@/lib/api';
import type { Recipe } from '@/types';
import { getDifficultyLabel, formatCookingTime } from '@/lib/utils';
import EmptyState from '@/components/ui/EmptyState';
import { SkeletonCard } from '@/components/ui/LoadingSpinner';

export default function BookmarksPage() {
  const router = useRouter();
  const [bookmarks, setBookmarks] = useState<Recipe[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchBookmarks() {
      try {
        const res = await api.getBookmarks();
        if (res.success && res.data) {
          setBookmarks(res.data);
        }
      } catch {
        // silent
      } finally {
        setLoading(false);
      }
    }
    fetchBookmarks();
  }, []);

  return (
    <div className="min-h-screen bg-surface-variant">
      <header
        className="bg-surface px-4 pt-12 pb-4 flex items-center gap-3"
        style={{ boxShadow: 'var(--shadow-level-1)' }}
      >
        <button onClick={() => router.back()} className="p-2 -ml-2">
          <ArrowLeft size={24} className="text-on-surface" />
        </button>
        <h1 className="text-lg font-semibold text-on-surface">내 북마크</h1>
      </header>

      <div className="px-4 py-4">
        {loading ? (
          <div className="grid grid-cols-2 gap-3">
            {[1, 2, 3, 4].map((i) => (
              <SkeletonCard key={i} />
            ))}
          </div>
        ) : bookmarks.length === 0 ? (
          <EmptyState
            icon={<span className="text-6xl">📑</span>}
            title="아직 저장한 레시피가 없어요"
            description="마음에 드는 레시피를 북마크해보세요"
            ctaLabel="레시피 둘러보기"
            onCtaClick={() => router.push('/recipe')}
          />
        ) : (
          <div className="grid grid-cols-2 gap-3">
            {bookmarks.map((recipe) => (
              <div
                key={recipe.id}
                className="bg-surface rounded-[20px] overflow-hidden cursor-pointer active:scale-[0.97] transition-transform"
                style={{ boxShadow: 'var(--shadow-level-1)', transitionDuration: 'var(--duration-fast)' }}
                onClick={() => router.push(`/recipe/${recipe.id}`)}
              >
                <div className="h-36 bg-gradient-to-br from-primary-300 to-primary flex items-center justify-center">
                  <span className="text-4xl">🍽️</span>
                </div>
                <div className="p-3">
                  <p className="font-medium text-on-surface text-sm line-clamp-1 mb-1.5">
                    {recipe.title}
                  </p>
                  <div className="flex gap-1.5 mb-2 flex-wrap">
                    {recipe.cuisineType && (
                      <span className="text-xs px-2 py-0.5 rounded-lg bg-primary/10 text-primary">
                        {recipe.cuisineType}
                      </span>
                    )}
                    {recipe.difficulty && (
                      <span className="text-xs px-2 py-0.5 rounded-lg bg-surface-variant text-on-surface-variant">
                        {getDifficultyLabel(recipe.difficulty)}
                      </span>
                    )}
                  </div>
                  <div className="flex items-center gap-1 text-xs text-on-surface-variant">
                    <Clock size={12} />
                    <span>{formatCookingTime(recipe.cookingTime) || '-'}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
