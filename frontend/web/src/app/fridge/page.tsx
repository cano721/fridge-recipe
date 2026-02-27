'use client';

import { useEffect, useState, useCallback } from 'react';
import { useRouter } from 'next/navigation';
import { api } from '@/lib/api';
import { useAuth } from '@/lib/auth-context';
import { Ingredient, IngredientMaster, StorageType } from '@/types';
import { getCategoryEmoji, getStorageLabel, getStorageEmoji } from '@/lib/utils';
import ExpiryBadge from '@/components/ui/ExpiryBadge';
import EmptyState from '@/components/ui/EmptyState';
import { SkeletonListItem } from '@/components/ui/LoadingSpinner';
import { Trash2, X, Search, Plus } from 'lucide-react';

type StorageTab = StorageType | null;

const storageTabs: { label: string; value: StorageTab }[] = [
  { label: '전체', value: null },
  { label: '냉장', value: 'fridge' },
  { label: '냉동', value: 'freezer' },
  { label: '실온', value: 'room' },
];

function groupByCategory(items: Ingredient[]): Record<string, Ingredient[]> {
  return items.reduce<Record<string, Ingredient[]>>((acc, item) => {
    const cat = item.category || '기타';
    if (!acc[cat]) acc[cat] = [];
    acc[cat].push(item);
    return acc;
  }, {});
}

interface AddModalProps {
  onClose: () => void;
  onAdded: () => void;
}

function AddIngredientModal({ onClose, onAdded }: AddModalProps) {
  const [query, setQuery] = useState('');
  const [searchResults, setSearchResults] = useState<IngredientMaster[]>([]);
  const [selected, setSelected] = useState<IngredientMaster | null>(null);
  const [quantity, setQuantity] = useState('');
  const [unit, setUnit] = useState('');
  const [expiryDate, setExpiryDate] = useState('');
  const [storageType, setStorageType] = useState<StorageType>('fridge');
  const [searching, setSearching] = useState(false);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (!query.trim()) {
      setSearchResults([]);
      return;
    }
    const timer = setTimeout(async () => {
      setSearching(true);
      try {
        const res = await api.searchIngredientMaster(query);
        if (res?.data) setSearchResults(res.data);
        else setSearchResults([]);
      } catch {
        setSearchResults([]);
      } finally {
        setSearching(false);
      }
    }, 300);
    return () => clearTimeout(timer);
  }, [query]);

  function handleSelect(master: IngredientMaster) {
    setSelected(master);
    setUnit(master.defaultUnit || '');
    setSearchResults([]);
    setQuery(master.name);
  }

  async function handleSubmit(e: React.FormEvent) {
    e.preventDefault();
    if (!selected) return;
    setSubmitting(true);
    try {
      await api.addIngredient({
        ingredientId: selected.id,
        quantity: quantity ? Number(quantity) : undefined,
        unit: unit || undefined,
        expiryDate: expiryDate || undefined,
        storageType,
      });
      onAdded();
      onClose();
    } catch {
      // silent
    } finally {
      setSubmitting(false);
    }
  }

  return (
    <div className="fixed inset-0 z-50 flex flex-col justify-end scrim" onClick={onClose}>
      <div
        className="bg-surface rounded-t-[28px] p-5 space-y-4 max-h-[85vh] overflow-y-auto"
        style={{ boxShadow: 'var(--shadow-level-5)' }}
        onClick={(e) => e.stopPropagation()}
      >
        <div className="w-8 h-1 bg-on-surface-variant/40 rounded-full mx-auto mb-3" style={{ marginTop: '12px' }} />
        <div className="flex items-center justify-between">
          <h3 className="text-lg font-bold text-on-surface">식재료 추가</h3>
          <button onClick={onClose} className="p-2 text-on-surface-variant rounded-full hover:bg-surface-variant">
            <X size={20} />
          </button>
        </div>

        <div className="relative">
          <Search size={16} className="absolute left-4 top-1/2 -translate-y-1/2 text-on-surface-variant" />
          <input
            type="text"
            placeholder="식재료 검색..."
            value={query}
            onChange={(e) => {
              setQuery(e.target.value);
              setSelected(null);
            }}
            className="w-full h-14 pl-10 pr-4 border-[1.5px] border-outline rounded-xl text-sm focus:outline-none focus:border-primary focus:border-2 bg-surface"
          />
        </div>

        {searching && (
          <p className="text-xs text-on-surface-variant text-center py-2">검색 중...</p>
        )}
        {!searching && searchResults.length > 0 && (
          <ul className="border border-outline rounded-xl overflow-hidden divide-y divide-outline-variant">
            {searchResults.map((item) => (
              <li key={item.id}>
                <button
                  type="button"
                  onClick={() => handleSelect(item)}
                  className="w-full px-4 py-3 text-left flex items-center gap-3 hover:bg-surface-variant active:bg-surface-variant transition-colors"
                >
                  <span className="text-xl">{getCategoryEmoji(item.category)}</span>
                  <div>
                    <p className="text-sm font-medium text-on-surface">{item.name}</p>
                    <p className="text-xs text-on-surface-variant">{item.category}</p>
                  </div>
                </button>
              </li>
            ))}
          </ul>
        )}

        {selected && (
          <form onSubmit={handleSubmit} className="space-y-3">
            <div className="flex items-center gap-2 p-3 bg-primary-50 rounded-xl">
              <span className="text-xl">{getCategoryEmoji(selected.category)}</span>
              <span className="text-sm font-semibold text-primary">{selected.name}</span>
            </div>

            <div className="grid grid-cols-2 gap-3">
              <div>
                <label className="text-xs text-on-surface-variant mb-1 block">수량</label>
                <input
                  type="number"
                  placeholder="0"
                  value={quantity}
                  onChange={(e) => setQuantity(e.target.value)}
                  className="w-full h-14 px-4 border-[1.5px] border-outline rounded-xl text-sm focus:outline-none focus:border-primary focus:border-2 bg-surface"
                />
              </div>
              <div>
                <label className="text-xs text-on-surface-variant mb-1 block">단위</label>
                <input
                  type="text"
                  placeholder="개, g, ml..."
                  value={unit}
                  onChange={(e) => setUnit(e.target.value)}
                  className="w-full h-14 px-4 border-[1.5px] border-outline rounded-xl text-sm focus:outline-none focus:border-primary focus:border-2 bg-surface"
                />
              </div>
            </div>

            <div>
              <label className="text-xs text-on-surface-variant mb-1 block">소비기한</label>
              <input
                type="date"
                value={expiryDate}
                onChange={(e) => setExpiryDate(e.target.value)}
                className="w-full h-14 px-4 border-[1.5px] border-outline rounded-xl text-sm focus:outline-none focus:border-primary focus:border-2 bg-surface"
              />
            </div>

            <div>
              <label className="text-xs text-on-surface-variant mb-1 block">보관 방법</label>
              <div className="grid grid-cols-3 gap-2">
                {(['fridge', 'freezer', 'room'] as StorageType[]).map((type) => (
                  <button
                    key={type}
                    type="button"
                    onClick={() => setStorageType(type)}
                    className={`h-10 rounded-lg text-xs font-medium border transition-colors ${
                      storageType === type
                        ? 'bg-primary text-white border-primary'
                        : 'bg-surface border-outline text-on-surface-variant'
                    }`}
                    style={{ transitionDuration: 'var(--duration-fast)' }}
                  >
                    {getStorageEmoji(type)} {getStorageLabel(type)}
                  </button>
                ))}
              </div>
            </div>

            <button
              type="submit"
              disabled={submitting}
              className="w-full h-12 bg-primary text-white rounded-xl font-medium text-sm disabled:opacity-40 active:bg-primary-600 active:scale-[0.97] transition-all"
              style={{ transitionDuration: 'var(--duration-fast)' }}
            >
              {submitting ? '추가 중...' : '냉장고에 추가하기'}
            </button>
          </form>
        )}
      </div>
    </div>
  );
}

export default function FridgePage() {
  const router = useRouter();
  const { isLoggedIn, isLoading: authLoading } = useAuth();
  const [selectedStorage, setSelectedStorage] = useState<StorageTab>(null);
  const [ingredients, setIngredients] = useState<Ingredient[]>([]);
  const [loading, setLoading] = useState(true);
  const [showModal, setShowModal] = useState(false);

  const fetchIngredients = useCallback(async () => {
    if (!isLoggedIn) {
      setLoading(false);
      return;
    }
    setLoading(true);
    try {
      const res = await api.getIngredients(selectedStorage ?? undefined);
      if (res?.data) setIngredients(res.data);
      else setIngredients([]);
    } catch {
      setIngredients([]);
    } finally {
      setLoading(false);
    }
  }, [selectedStorage, isLoggedIn]);

  useEffect(() => {
    if (!authLoading) fetchIngredients();
  }, [fetchIngredients, authLoading]);

  async function handleDelete(id: number) {
    try {
      await api.deleteIngredient(id);
      setIngredients((prev) => prev.filter((item) => item.id !== id));
    } catch {
      // silent
    }
  }

  // Auth guard
  if (!authLoading && !isLoggedIn) {
    return (
      <div className="min-h-screen bg-surface-variant">
        <header className="bg-surface px-5 pt-12 pb-4" style={{ boxShadow: 'var(--shadow-level-1)' }}>
          <h1 className="text-[22px] font-semibold text-on-surface">내 냉장고</h1>
        </header>
        <div className="px-4 py-8">
          <EmptyState
            icon={<span className="text-6xl">🔒</span>}
            title="로그인이 필요해요"
            description="내 냉장고에 식재료를 등록하려면 로그인해주세요"
            ctaLabel="로그인하기"
            onCtaClick={() => router.push('/login')}
          />
        </div>
      </div>
    );
  }

  const grouped = groupByCategory(ingredients);

  return (
    <div className="min-h-screen bg-surface-variant">
      <header className="bg-surface px-5 pt-12 pb-4" style={{ boxShadow: 'var(--shadow-level-1)' }}>
        <h1 className="text-[22px] font-semibold text-on-surface">내 냉장고</h1>

        <div className="flex gap-2 mt-4 overflow-x-auto pb-1 scrollbar-none">
          {storageTabs.map((tab) => (
            <button
              key={String(tab.value)}
              onClick={() => setSelectedStorage(tab.value)}
              className={`flex-shrink-0 h-8 px-3 rounded-lg text-sm font-medium border transition-colors ${
                selectedStorage === tab.value
                  ? 'bg-secondary-100 text-on-surface border-transparent'
                  : 'bg-transparent text-on-surface-variant border-outline'
              }`}
              style={{ transitionDuration: 'var(--duration-fast)' }}
            >
              {selectedStorage === tab.value && <span className="mr-1">✓</span>}
              {tab.label}
            </button>
          ))}
        </div>
      </header>

      <div className="px-4 py-4 pb-24 space-y-4">
        {loading ? (
          <div className="space-y-3">
            {[1, 2, 3].map((i) => (
              <SkeletonListItem key={i} />
            ))}
          </div>
        ) : ingredients.length === 0 ? (
          <EmptyState
            icon={<span className="text-6xl">🧊</span>}
            title="냉장고가 텅 비었어요"
            description="재료를 추가하면 맞춤 레시피를 추천해요"
            ctaLabel="재료 추가하기"
            onCtaClick={() => setShowModal(true)}
          />
        ) : (
          Object.entries(grouped).map(([category, items]) => (
            <section key={category}>
              <h2 className="text-sm font-semibold text-on-surface-variant mb-2 flex items-center gap-1.5">
                <span>{getCategoryEmoji(category)}</span>
                <span>{category}</span>
              </h2>
              <div className="space-y-2">
                {items.map((item) => (
                  <div
                    key={item.id}
                    className="bg-surface rounded-2xl px-4 py-3 flex items-center justify-between"
                    style={{ boxShadow: 'var(--shadow-level-1)' }}
                  >
                    <div className="flex items-center gap-3 min-w-0">
                      <span className="w-12 h-12 rounded-xl bg-surface-variant flex items-center justify-center text-2xl flex-shrink-0">
                        {getCategoryEmoji(item.category)}
                      </span>
                      <div className="min-w-0">
                        <p className="text-sm font-semibold text-on-surface truncate">{item.name}</p>
                        <div className="flex items-center gap-2 mt-0.5 flex-wrap">
                          {item.quantity != null && (
                            <span className="text-xs text-on-surface-variant">
                              {item.quantity}{item.unit || ''}
                            </span>
                          )}
                          <span className="text-xs text-on-surface-variant bg-surface-variant px-1.5 py-0.5 rounded-[4px]">
                            {getStorageEmoji(item.storageType)} {getStorageLabel(item.storageType)}
                          </span>
                          <ExpiryBadge daysUntilExpiry={item.daysUntilExpiry} />
                        </div>
                      </div>
                    </div>
                    <button
                      onClick={() => handleDelete(item.id)}
                      className="flex-shrink-0 ml-2 p-2 text-on-surface-variant hover:text-danger active:scale-90 transition-transform"
                    >
                      <Trash2 size={16} />
                    </button>
                  </div>
                ))}
              </div>
            </section>
          ))
        )}
      </div>

      <button
        onClick={() => setShowModal(true)}
        className="fixed bottom-24 z-40 w-14 h-14 rounded-2xl flex items-center justify-center active:scale-95 active:bg-primary-200 transition-all"
        style={{
          right: 'calc(50% - min(50vw, 224px) + 16px)',
          backgroundColor: '#DCEFDC',
          boxShadow: 'var(--shadow-level-3)',
          transitionDuration: 'var(--duration-fast)',
        }}
      >
        <Plus size={24} className="text-primary-900" />
      </button>

      {showModal && (
        <AddIngredientModal
          onClose={() => setShowModal(false)}
          onAdded={fetchIngredients}
        />
      )}
    </div>
  );
}
