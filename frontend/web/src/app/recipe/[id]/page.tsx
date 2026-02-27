"use client";

import { useState, useEffect, use } from "react";
import { useRouter } from "next/navigation";
import { ArrowLeft, Heart, Clock, Users, ChefHat, Star } from "lucide-react";
import { api } from "@/lib/api";
import type { RecipeDetail } from "@/types";
import { getDifficultyLabel, formatCookingTime } from "@/lib/utils";
import EmptyState from "@/components/ui/EmptyState";
import { Skeleton } from "@/components/ui/LoadingSpinner";

interface PageProps {
  params: Promise<{ id: string }>;
}

export default function RecipeDetailPage({ params }: PageProps) {
  const { id } = use(params);
  const router = useRouter();
  const [recipe, setRecipe] = useState<RecipeDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [bookmarked, setBookmarked] = useState(false);
  const [bookmarkLoading, setBookmarkLoading] = useState(false);

  useEffect(() => {
    loadRecipe();
  }, [id]);

  async function loadRecipe() {
    setLoading(true);
    try {
      const res = await api.getRecipeDetail(Number(id));
      if (res.success && res.data) {
        setRecipe(res.data);
        setBookmarked(res.data.isBookmarked || false);
      }
    } finally {
      setLoading(false);
    }
  }

  async function handleBookmark() {
    if (bookmarkLoading) return;
    setBookmarkLoading(true);
    try {
      if (bookmarked) {
        await api.removeBookmark(Number(id));
      } else {
        await api.addBookmark(Number(id));
      }
      setBookmarked((prev) => !prev);
    } finally {
      setBookmarkLoading(false);
    }
  }

  if (loading) {
    return (
      <div className="min-h-screen bg-surface">
        <Skeleton className="h-56 rounded-none" />
        <div className="px-4 pt-4 space-y-4">
          <Skeleton className="h-8 w-3/4 rounded-lg" />
          <div className="flex gap-2">
            <Skeleton className="h-8 w-20 rounded-lg" />
            <Skeleton className="h-8 w-20 rounded-lg" />
            <Skeleton className="h-8 w-20 rounded-lg" />
          </div>
          <Skeleton className="h-4 w-full rounded" />
          <Skeleton className="h-4 w-2/3 rounded" />
          <Skeleton className="h-32 rounded-xl" />
        </div>
      </div>
    );
  }

  if (!recipe) {
    return (
      <div className="min-h-screen bg-surface flex flex-col">
        <header className="h-16 flex items-center px-4">
          <button onClick={() => router.back()} className="p-2 -ml-2">
            <ArrowLeft size={24} className="text-on-surface" />
          </button>
        </header>
        <div className="flex-1 flex items-center justify-center">
          <EmptyState
            icon={<span className="text-6xl">😕</span>}
            title="레시피를 불러올 수 없습니다"
            description="잠시 후 다시 시도해주세요"
            ctaLabel="돌아가기"
            onCtaClick={() => router.back()}
          />
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-surface pb-8">
      {/* Hero section */}
      <div className="relative h-56">
        <div className="absolute inset-0 bg-gradient-to-br from-primary-300 to-primary flex items-center justify-center">
          <span className="text-7xl">🍽️</span>
        </div>

        {/* Gradient overlay at bottom */}
        <div className="absolute inset-x-0 bottom-0 h-24 bg-gradient-to-t from-black/60 to-transparent" />

        {/* Back button */}
        <button
          onClick={() => router.back()}
          className="absolute top-4 left-4 w-12 h-12 rounded-full bg-black/30 backdrop-blur-sm flex items-center justify-center"
        >
          <ArrowLeft size={20} className="text-white" />
        </button>

        {/* Bookmark button */}
        <button
          onClick={handleBookmark}
          disabled={bookmarkLoading}
          className="absolute top-4 right-4 w-12 h-12 rounded-full bg-black/30 backdrop-blur-sm flex items-center justify-center active:scale-[0.95] transition-transform"
          style={{ transitionDuration: 'var(--duration-medium)' }}
        >
          <Heart
            size={20}
            className={bookmarked ? "text-accent fill-accent" : "text-white"}
          />
        </button>

        {/* Title overlay */}
        <div className="absolute inset-x-0 bottom-0 px-4 pb-3">
          <h1 className="text-white font-bold text-lg leading-tight drop-shadow">
            {recipe.title}
          </h1>
        </div>
      </div>

      {/* Content */}
      <div className="px-4 pt-4 space-y-5">
        {/* Info row - Chips with 8dp radius */}
        <div className="flex items-center gap-2 flex-wrap">
          {recipe.difficulty && (
            <div className="flex items-center gap-1.5 bg-surface-variant rounded-lg px-3 h-8">
              <ChefHat size={14} className="text-on-surface-variant" />
              <span className="text-xs text-on-surface-variant">
                {getDifficultyLabel(recipe.difficulty)}
              </span>
            </div>
          )}
          {recipe.cookingTime && (
            <div className="flex items-center gap-1.5 bg-surface-variant rounded-lg px-3 h-8">
              <Clock size={14} className="text-on-surface-variant" />
              <span className="text-xs text-on-surface-variant">
                {formatCookingTime(recipe.cookingTime)}
              </span>
            </div>
          )}
          <div className="flex items-center gap-1.5 bg-surface-variant rounded-lg px-3 h-8">
            <Users size={14} className="text-on-surface-variant" />
            <span className="text-xs text-on-surface-variant">{recipe.servings}인분</span>
          </div>
          {recipe.avgRating > 0 && (
            <div className="flex items-center gap-1.5 bg-surface-variant rounded-lg px-3 h-8">
              <Star size={14} className="text-secondary fill-secondary" />
              <span className="text-xs text-on-surface-variant">
                {recipe.avgRating.toFixed(1)}
              </span>
            </div>
          )}
        </div>

        {/* Tags - 8dp radius */}
        {recipe.tags && recipe.tags.length > 0 && (
          <div className="flex gap-2 flex-wrap">
            {recipe.tags.map((tag) => (
              <span
                key={tag}
                className="text-xs px-3 h-8 inline-flex items-center rounded-lg border border-outline text-on-surface-variant"
              >
                #{tag}
              </span>
            ))}
          </div>
        )}

        {/* Description */}
        {recipe.description && (
          <p className="text-sm text-on-surface-variant leading-relaxed">
            {recipe.description}
          </p>
        )}

        {/* Ingredients section */}
        <section>
          <h2 className="text-base font-bold text-on-surface mb-3">재료</h2>
          <div className="bg-surface-variant rounded-2xl p-4 space-y-0">
            {recipe.ingredients.map((ing) => (
              <div
                key={ing.ingredientId}
                className="flex items-center justify-between py-2.5 border-b border-outline-variant last:border-0"
              >
                <div className="flex items-center gap-2">
                  {ing.hasIngredient ? (
                    <span className="w-5 h-5 rounded-full bg-safe/15 flex items-center justify-center text-[10px] text-safe font-bold">
                      ✓
                    </span>
                  ) : (
                    <span className="w-5 h-5 rounded-full bg-danger/15 flex items-center justify-center text-[10px] text-danger font-bold">
                      ✕
                    </span>
                  )}
                  <span className="text-sm text-on-surface">{ing.name}</span>
                  {ing.isEssential && (
                    <Star size={12} className="text-secondary fill-secondary" />
                  )}
                </div>
                {ing.quantity && (
                  <span className="text-sm text-on-surface-variant">{ing.quantity}</span>
                )}
              </div>
            ))}
          </div>
        </section>

        {/* Steps section */}
        {recipe.steps && recipe.steps.length > 0 && (
          <section>
            <h2 className="text-base font-bold text-on-surface mb-3">조리 순서</h2>
            <div className="space-y-3">
              {recipe.steps.map((step) => (
                <div
                  key={step.order}
                  className="flex gap-3 bg-surface-variant rounded-xl p-3"
                >
                  <div className="w-7 h-7 rounded-full bg-primary flex items-center justify-center shrink-0 mt-0.5">
                    <span className="text-xs font-bold text-white">{step.order}</span>
                  </div>
                  <p className="text-sm text-on-surface leading-relaxed flex-1">
                    {step.description}
                  </p>
                </div>
              ))}
            </div>
          </section>
        )}

        {/* Nutrition section */}
        {recipe.nutrition && (
          <section>
            <h2 className="text-base font-bold text-on-surface mb-3">영양 정보</h2>
            <div className="grid grid-cols-4 gap-2">
              {[
                { label: "칼로리", value: recipe.nutrition.calories, unit: "kcal" },
                { label: "단백질", value: recipe.nutrition.protein, unit: "g" },
                { label: "지방", value: recipe.nutrition.fat, unit: "g" },
                { label: "탄수화물", value: recipe.nutrition.carbs, unit: "g" },
              ].map(({ label, value, unit }) => (
                <div
                  key={label}
                  className="bg-surface-variant rounded-xl p-3 flex flex-col items-center gap-0.5"
                >
                  <span className="text-xs text-on-surface-variant">{label}</span>
                  <span className="text-sm font-bold text-on-surface">{value}</span>
                  <span className="text-xs text-on-surface-variant">{unit}</span>
                </div>
              ))}
            </div>
          </section>
        )}

        {/* CTA Button */}
        <button
          className="w-full h-12 bg-primary text-white rounded-xl font-medium text-sm active:bg-primary-600 active:scale-[0.97] transition-all"
          style={{ transitionDuration: 'var(--duration-fast)' }}
        >
          요리 시작하기 🍳
        </button>
      </div>
    </div>
  );
}
