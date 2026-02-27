"use client";

import { useState, useEffect, FormEvent } from "react";
import { useRouter } from "next/navigation";
import { Search, Clock } from "lucide-react";
import { api } from "@/lib/api";
import type { Recipe, RecipeRecommendation } from "@/types";
import { getDifficultyLabel, formatCookingTime } from "@/lib/utils";
import EmptyState from "@/components/ui/EmptyState";
import { SkeletonCard } from "@/components/ui/LoadingSpinner";

const CUISINE_FILTERS = [
  { label: "전체", value: "" },
  { label: "한식", value: "korean" },
  { label: "중식", value: "chinese" },
  { label: "일식", value: "japanese" },
  { label: "양식", value: "western" },
];

const DIFFICULTY_FILTERS = [
  { label: "전체", value: "" },
  { label: "쉬움", value: "easy" },
  { label: "보통", value: "medium" },
  { label: "어려움", value: "hard" },
];

const TIME_FILTERS = [
  { label: "전체", value: 0 },
  { label: "15분 이내", value: 15 },
  { label: "30분 이내", value: 30 },
  { label: "1시간 이내", value: 60 },
];

const SORT_OPTIONS = [
  { label: "추천순", value: "" },
  { label: "평점순", value: "rating" },
  { label: "조리시간순", value: "cookingTime" },
  { label: "최신순", value: "newest" },
];

function getMatchLabel(matchLabel: string): { text: string; color: string } {
  switch (matchLabel) {
    case "perfect":
      return { text: "완벽 매치", color: "bg-primary text-white" };
    case "great":
      return { text: "좋은 매치", color: "bg-secondary text-white" };
    case "good":
      return { text: "매치", color: "bg-secondary-300 text-secondary-700" };
    default:
      return { text: matchLabel, color: "bg-surface-variant text-on-surface-variant" };
  }
}

interface RecipeCardProps {
  recipe: Recipe;
  matchRatio?: number;
  matchLabel?: string;
}

function RecipeCard({ recipe, matchRatio, matchLabel }: RecipeCardProps) {
  const router = useRouter();

  const matchInfo = matchLabel ? getMatchLabel(matchLabel) : null;

  return (
    <div
      className="bg-surface rounded-[20px] overflow-hidden cursor-pointer active:scale-[0.97] transition-transform"
      style={{ boxShadow: 'var(--shadow-level-1)', transitionDuration: 'var(--duration-fast)' }}
      onClick={() => router.push(`/recipe/${recipe.id}`)}
    >
      {/* Thumbnail */}
      <div className="h-36 bg-gradient-to-br from-primary-300 to-primary flex items-center justify-center">
        <span className="text-4xl">🍽️</span>
      </div>

      <div className="p-3">
        {/* Title */}
        <p className="font-medium text-on-surface text-sm line-clamp-1 mb-1.5">
          {recipe.title}
        </p>

        {/* Tags row - Chip: 8dp radius */}
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

        {/* Bottom row */}
        <div className="flex items-center justify-between gap-2">
          {/* Cooking time */}
          <div className="flex items-center gap-1 text-xs text-on-surface-variant shrink-0">
            <Clock size={12} />
            <span>{formatCookingTime(recipe.cookingTime) || "-"}</span>
          </div>

          {/* Match ratio */}
          {matchRatio !== undefined && (
            <div className="flex items-center gap-1.5 flex-1 min-w-0">
              <div className="flex-1 h-1.5 bg-outline-variant rounded-full overflow-hidden">
                <div
                  className="h-full bg-primary rounded-full"
                  style={{ width: `${Math.round(matchRatio * 100)}%` }}
                />
              </div>
              <span className="text-xs text-on-surface-variant shrink-0">
                {Math.round(matchRatio * 100)}%
              </span>
            </div>
          )}
        </div>

        {/* Match label badge */}
        {matchInfo && (
          <div className="mt-2">
            <span className={`text-xs px-2 py-0.5 rounded-lg ${matchInfo.color}`}>
              {matchInfo.text}
            </span>
          </div>
        )}
      </div>
    </div>
  );
}

interface Ingredient {
  id: number;
  name: string;
}

export default function RecipePage() {
  const router = useRouter();
  const [query, setQuery] = useState("");
  const [activeQuery, setActiveQuery] = useState("");
  const [activeCuisine, setActiveCuisine] = useState("");
  const [activeDifficulty, setActiveDifficulty] = useState("");
  const [activeTimeFilter, setActiveTimeFilter] = useState(0);
  const [activeSort, setActiveSort] = useState("");
  const [myIngredients, setMyIngredients] = useState<Ingredient[]>([]);
  const [selectedIngredientIds, setSelectedIngredientIds] = useState<number[]>([]);
  const [recommendations, setRecommendations] = useState<RecipeRecommendation[]>([]);
  const [searchResults, setSearchResults] = useState<Recipe[]>([]);
  const [loading, setLoading] = useState(false);
  const [searchActive, setSearchActive] = useState(false);

  useEffect(() => {
    loadRecommendations();
    loadIngredients();
  }, []);

  async function loadRecommendations() {
    setLoading(true);
    try {
      const res = await api.getRecommendations(20);
      if (res.success && res.data) {
        setRecommendations(res.data);
      }
    } finally {
      setLoading(false);
    }
  }

  async function loadIngredients() {
    try {
      const res = await api.getIngredients();
      if (res.success && res.data) {
        setMyIngredients(res.data as Ingredient[]);
      }
    } catch {
      // 재료 로드 실패 시 무시
    }
  }

  async function searchWithFilters(
    q: string,
    overrides?: {
      cuisineType?: string;
      difficulty?: string;
      maxCookingTime?: number;
      ingredientIds?: number[];
      sort?: string;
    }
  ) {
    setLoading(true);
    try {
      const res = await api.searchRecipes(q, {
        cuisineType: (overrides?.cuisineType ?? activeCuisine) || undefined,
        difficulty: (overrides?.difficulty ?? activeDifficulty) || undefined,
        maxCookingTime: (overrides?.maxCookingTime ?? activeTimeFilter) || undefined,
        ingredientIds:
          (overrides?.ingredientIds ?? selectedIngredientIds).length > 0
            ? (overrides?.ingredientIds ?? selectedIngredientIds)
            : undefined,
        sort: (overrides?.sort ?? activeSort) || undefined,
      });
      if (res.success && res.data) {
        setSearchResults(res.data);
      }
    } finally {
      setLoading(false);
    }
  }

  async function handleSearch(e: FormEvent) {
    e.preventDefault();
    if (!query.trim()) return;
    setActiveQuery(query.trim());
    setSearchActive(true);
    await searchWithFilters(query.trim());
  }

  async function handleCuisineFilter(value: string) {
    setActiveCuisine(value);
    if (searchActive && activeQuery) {
      await searchWithFilters(activeQuery, { cuisineType: value });
    }
  }

  async function handleDifficultyFilter(value: string) {
    setActiveDifficulty(value);
    if (searchActive && activeQuery) {
      await searchWithFilters(activeQuery, { difficulty: value });
    }
  }

  async function handleTimeFilter(value: number) {
    setActiveTimeFilter(value);
    if (searchActive && activeQuery) {
      await searchWithFilters(activeQuery, { maxCookingTime: value });
    }
  }

  async function handleSortChange(value: string) {
    setActiveSort(value);
    if (searchActive && activeQuery) {
      await searchWithFilters(activeQuery, { sort: value });
    }
  }

  async function handleIngredientToggle(id: number) {
    const next = selectedIngredientIds.includes(id)
      ? selectedIngredientIds.filter((i) => i !== id)
      : [...selectedIngredientIds, id];
    setSelectedIngredientIds(next);
    if (searchActive && activeQuery) {
      await searchWithFilters(activeQuery, { ingredientIds: next });
    }
  }

  function handleClearSearch() {
    setQuery("");
    setActiveQuery("");
    setSearchActive(false);
    setSearchResults([]);
  }

  const showRecommendations = !searchActive;

  return (
    <div className="min-h-screen bg-surface">
      {/* Header */}
      <div className="sticky top-0 bg-surface z-10" style={{ boxShadow: 'var(--shadow-level-2)' }}>
        <div className="px-4 pt-12 pb-3">
          <h1 className="text-[22px] font-semibold text-on-surface mb-3">레시피</h1>

          {/* Search bar - rounded full per spec */}
          <form onSubmit={handleSearch} className="relative">
            <Search
              size={18}
              className="absolute left-4 top-1/2 -translate-y-1/2 text-on-surface-variant"
            />
            <input
              type="text"
              value={query}
              onChange={(e) => setQuery(e.target.value)}
              placeholder="레시피나 재료를 검색해보세요"
              className="w-full pl-11 pr-16 h-12 bg-surface-variant rounded-full text-sm text-on-surface placeholder:text-on-surface-variant/60 outline-none focus:ring-2 focus:ring-primary/30"
            />
            {searchActive && (
              <button
                type="button"
                onClick={handleClearSearch}
                className="absolute right-4 top-1/2 -translate-y-1/2 text-xs text-primary font-medium"
              >
                취소
              </button>
            )}
          </form>
        </div>

        {/* 음식 종류 칩 행 */}
        <div className="flex gap-2 px-4 pb-2 overflow-x-auto scrollbar-hide">
          {CUISINE_FILTERS.map((f) => (
            <button
              key={f.value}
              onClick={() => handleCuisineFilter(f.value)}
              className={`shrink-0 h-8 px-3 rounded-lg text-sm font-medium border transition-colors ${
                activeCuisine === f.value
                  ? "bg-secondary-100 text-on-surface border-transparent"
                  : "bg-transparent text-on-surface-variant border-outline"
              }`}
              style={{ transitionDuration: 'var(--duration-fast)' }}
            >
              {activeCuisine === f.value && <span className="mr-1">✓</span>}
              {f.label}
            </button>
          ))}
        </div>

        {/* 난이도 + 조리시간 칩 행 */}
        <div className="flex gap-2 px-4 pb-2 overflow-x-auto scrollbar-hide">
          {DIFFICULTY_FILTERS.map((f) => (
            <button
              key={f.value}
              onClick={() => handleDifficultyFilter(f.value)}
              className={`shrink-0 h-8 px-3 rounded-lg text-sm font-medium border transition-colors ${
                activeDifficulty === f.value
                  ? "bg-secondary-100 text-on-surface border-transparent"
                  : "bg-transparent text-on-surface-variant border-outline"
              }`}
              style={{ transitionDuration: 'var(--duration-fast)' }}
            >
              {activeDifficulty === f.value && f.value !== "" && <span className="mr-1">✓</span>}
              {f.label}
            </button>
          ))}
          <div className="w-px h-8 bg-outline-variant shrink-0" />
          {TIME_FILTERS.map((f) => (
            <button
              key={f.value}
              onClick={() => handleTimeFilter(f.value)}
              className={`shrink-0 h-8 px-3 rounded-lg text-sm font-medium border transition-colors ${
                activeTimeFilter === f.value
                  ? "bg-secondary-100 text-on-surface border-transparent"
                  : "bg-transparent text-on-surface-variant border-outline"
              }`}
              style={{ transitionDuration: 'var(--duration-fast)' }}
            >
              {activeTimeFilter === f.value && f.value !== 0 && <span className="mr-1">✓</span>}
              {f.label}
            </button>
          ))}
        </div>

        {/* 내 재료 칩 행 */}
        {myIngredients.length > 0 && (
          <div className="flex gap-2 px-4 pb-2 overflow-x-auto scrollbar-hide">
            <span className="shrink-0 h-8 flex items-center text-xs text-on-surface-variant font-medium pr-1">
              내 재료
            </span>
            {myIngredients.map((ing) => (
              <button
                key={ing.id}
                onClick={() => handleIngredientToggle(ing.id)}
                className={`shrink-0 h-8 px-3 rounded-lg text-sm font-medium border transition-colors ${
                  selectedIngredientIds.includes(ing.id)
                    ? "bg-secondary-100 text-on-surface border-transparent"
                    : "bg-transparent text-on-surface-variant border-outline"
                }`}
                style={{ transitionDuration: 'var(--duration-fast)' }}
              >
                {selectedIngredientIds.includes(ing.id) && <span className="mr-1">✓</span>}
                {ing.name}
              </button>
            ))}
          </div>
        )}

        {/* 정렬 옵션 */}
        <div className="flex justify-end px-4 pb-2">
          <div className="flex gap-1.5 overflow-x-auto scrollbar-hide">
            {SORT_OPTIONS.map((s) => (
              <button
                key={s.value}
                onClick={() => handleSortChange(s.value)}
                className={`shrink-0 h-7 px-2.5 rounded-lg text-xs font-medium border transition-colors ${
                  activeSort === s.value
                    ? "bg-secondary-100 text-on-surface border-transparent"
                    : "bg-transparent text-on-surface-variant border-outline"
                }`}
                style={{ transitionDuration: 'var(--duration-fast)' }}
              >
                {activeSort === s.value && s.value !== "" && <span className="mr-1">✓</span>}
                {s.label}
              </button>
            ))}
          </div>
        </div>

        {/* Tab indicator */}
        <div className="flex border-t border-outline-variant">
          <button
            onClick={handleClearSearch}
            className={`flex-1 py-2 text-center text-sm font-medium transition-colors ${
              !searchActive ? "text-primary border-b-2 border-primary" : "text-on-surface-variant"
            }`}
          >
            추천
          </button>
          <div
            className={`flex-1 py-2 text-center text-sm font-medium transition-colors ${
              searchActive ? "text-primary border-b-2 border-primary" : "text-on-surface-variant"
            }`}
          >
            검색결과
          </div>
        </div>
      </div>

      {/* Content */}
      <div className="px-4 py-4">
        {loading ? (
          <div className="grid grid-cols-2 gap-3">
            {[1, 2, 3, 4].map((i) => (
              <SkeletonCard key={i} />
            ))}
          </div>
        ) : showRecommendations ? (
          recommendations.length === 0 ? (
            <EmptyState
              icon={<span className="text-6xl">🍽️</span>}
              title="재료가 부족해요"
              description="냉장고에 재료를 추가하면 맞춤 레시피를 추천해드려요"
              ctaLabel="재료 추가하기"
              onCtaClick={() => router.push('/fridge')}
            />
          ) : (
            <div className="grid grid-cols-2 gap-3">
              {recommendations.map((rec) => (
                <RecipeCard
                  key={rec.recipe.id}
                  recipe={rec.recipe}
                  matchRatio={rec.matchRatio}
                  matchLabel={rec.matchLabel}
                />
              ))}
            </div>
          )
        ) : searchResults.length === 0 ? (
          <EmptyState
            icon={<span className="text-6xl">🔍</span>}
            title="검색 결과가 없어요"
            description={`"${activeQuery}"에 대한 결과를 찾을 수 없습니다. 다른 키워드로 검색해보세요.`}
          />
        ) : (
          <div className="grid grid-cols-2 gap-3">
            {searchResults.map((recipe) => (
              <RecipeCard key={recipe.id} recipe={recipe} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
}
