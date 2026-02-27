'use client';

import { useEffect, useState } from 'react';
import { useRouter } from 'next/navigation';
import { ArrowLeft, Star, ChevronRight } from 'lucide-react';
import { api } from '@/lib/api';
import type { CookingHistory } from '@/types';
import EmptyState from '@/components/ui/EmptyState';
import { SkeletonListItem } from '@/components/ui/LoadingSpinner';

function formatDate(dateStr: string): string {
  const d = new Date(dateStr);
  const month = d.getMonth() + 1;
  const day = d.getDate();
  const weekDay = ['일', '월', '화', '수', '목', '금', '토'][d.getDay()];
  return `${month}월 ${day}일 (${weekDay})`;
}

function groupByDate(items: CookingHistory[]): Record<string, CookingHistory[]> {
  return items.reduce<Record<string, CookingHistory[]>>((acc, item) => {
    const key = item.cookedAt.split('T')[0];
    if (!acc[key]) acc[key] = [];
    acc[key].push(item);
    return acc;
  }, {});
}

export default function CookingHistoryPage() {
  const router = useRouter();
  const [history, setHistory] = useState<CookingHistory[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function fetchHistory() {
      try {
        const res = await api.getCookingHistory();
        if (res.success && res.data) {
          setHistory(res.data);
        }
      } catch {
        // silent
      } finally {
        setLoading(false);
      }
    }
    fetchHistory();
  }, []);

  const grouped = groupByDate(history);

  return (
    <div className="min-h-screen bg-surface-variant">
      <header
        className="bg-surface px-4 pt-12 pb-4 flex items-center gap-3"
        style={{ boxShadow: 'var(--shadow-level-1)' }}
      >
        <button onClick={() => router.back()} className="p-2 -ml-2">
          <ArrowLeft size={24} className="text-on-surface" />
        </button>
        <h1 className="text-lg font-semibold text-on-surface">조리 이력</h1>
      </header>

      <div className="px-4 py-4 space-y-4">
        {loading ? (
          <div className="space-y-3">
            {[1, 2, 3].map((i) => (
              <SkeletonListItem key={i} />
            ))}
          </div>
        ) : history.length === 0 ? (
          <EmptyState
            icon={<span className="text-6xl">👨‍🍳</span>}
            title="아직 조리 기록이 없어요"
            description="레시피를 따라 요리해보세요"
            ctaLabel="레시피 둘러보기"
            onCtaClick={() => router.push('/recipe')}
          />
        ) : (
          Object.entries(grouped).map(([date, items]) => (
            <section key={date}>
              <h2 className="text-sm font-semibold text-on-surface-variant mb-2">
                {formatDate(date)}
              </h2>
              <div className="space-y-2">
                {items.map((item) => (
                  <div
                    key={item.id}
                    className="bg-surface rounded-2xl px-4 py-3 flex items-center justify-between cursor-pointer active:scale-[0.98] transition-transform"
                    style={{ boxShadow: 'var(--shadow-level-1)', transitionDuration: 'var(--duration-fast)' }}
                    onClick={() => router.push(`/recipe/${item.recipeId}`)}
                  >
                    <div className="flex items-center gap-3 min-w-0 flex-1">
                      <span className="w-12 h-12 rounded-xl bg-primary-100 flex items-center justify-center text-2xl flex-shrink-0">
                        🍽️
                      </span>
                      <div className="min-w-0 flex-1">
                        <p className="text-sm font-semibold text-on-surface truncate">
                          {item.recipeTitle}
                        </p>
                        <div className="flex items-center gap-2 mt-0.5 flex-wrap">
                          {item.rating && (
                            <div className="flex items-center gap-0.5">
                              {Array.from({ length: 5 }).map((_, i) => (
                                <Star
                                  key={i}
                                  size={10}
                                  className={i < item.rating! ? 'text-secondary fill-secondary' : 'text-outline-variant'}
                                />
                              ))}
                            </div>
                          )}
                          {item.memo && (
                            <span className="text-xs text-on-surface-variant truncate max-w-[120px]">
                              {item.memo}
                            </span>
                          )}
                        </div>
                      </div>
                    </div>
                    <ChevronRight size={16} className="text-on-surface-variant flex-shrink-0" />
                  </div>
                ))}
              </div>
            </section>
          ))
        )}
      </div>
    </div>
  );
}
