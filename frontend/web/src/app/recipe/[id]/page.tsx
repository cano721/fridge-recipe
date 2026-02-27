"use client";

import { useState, useEffect, useCallback, useRef, use } from "react";
import { useRouter } from "next/navigation";
import { ArrowLeft, Heart, Clock, Users, ChefHat, Star, Play, Pause, RotateCcw, X, ChevronLeft, ChevronRight, Share2 } from "lucide-react";
import { api } from "@/lib/api";
import type { RecipeDetail, CookingStep } from "@/types";
import { getDifficultyLabel, formatCookingTime } from "@/lib/utils";
import EmptyState from "@/components/ui/EmptyState";
import { Skeleton } from "@/components/ui/LoadingSpinner";

interface PageProps {
  params: Promise<{ id: string }>;
}

// --- Timer Component ---
function StepTimer({ seconds, onComplete }: { seconds: number; onComplete?: () => void }) {
  const [remaining, setRemaining] = useState(seconds);
  const [running, setRunning] = useState(false);
  const intervalRef = useRef<NodeJS.Timeout | null>(null);

  useEffect(() => {
    if (running && remaining > 0) {
      intervalRef.current = setInterval(() => {
        setRemaining((prev) => {
          if (prev <= 1) {
            setRunning(false);
            onComplete?.();
            return 0;
          }
          return prev - 1;
        });
      }, 1000);
    }
    return () => { if (intervalRef.current) clearInterval(intervalRef.current); };
  }, [running, remaining, onComplete]);

  const formatTime = (s: number) => {
    const m = Math.floor(s / 60);
    const sec = s % 60;
    return `${m}:${sec.toString().padStart(2, '0')}`;
  };

  const progress = seconds > 0 ? ((seconds - remaining) / seconds) * 100 : 0;

  return (
    <div className="flex items-center gap-2 mt-2 bg-surface rounded-xl px-3 py-2">
      <div className="flex-1">
        <div className="flex items-center gap-2">
          <Clock size={14} className="text-primary" />
          <span className={`text-sm font-mono font-bold ${remaining === 0 ? 'text-safe' : 'text-on-surface'}`}>
            {remaining === 0 ? '완료!' : formatTime(remaining)}
          </span>
        </div>
        <div className="h-1.5 bg-outline-variant rounded-full mt-1.5 overflow-hidden">
          <div
            className="h-full bg-primary rounded-full transition-all duration-1000"
            style={{ width: `${progress}%` }}
          />
        </div>
      </div>
      <div className="flex gap-1">
        <button
          onClick={() => setRunning(!running)}
          className="w-8 h-8 rounded-full bg-primary-50 flex items-center justify-center"
          disabled={remaining === 0}
        >
          {running ? <Pause size={14} className="text-primary" /> : <Play size={14} className="text-primary ml-0.5" />}
        </button>
        <button
          onClick={() => { setRemaining(seconds); setRunning(false); }}
          className="w-8 h-8 rounded-full bg-surface-variant flex items-center justify-center"
        >
          <RotateCcw size={14} className="text-on-surface-variant" />
        </button>
      </div>
    </div>
  );
}

// --- Nutrition Bar ---
function NutritionBar({ label, value, unit, max, color }: {
  label: string; value: number; unit: string; max: number; color: string;
}) {
  const pct = max > 0 ? Math.min((value / max) * 100, 100) : 0;
  return (
    <div className="flex-1 flex flex-col items-center gap-1">
      <div className="w-full h-24 bg-outline-variant/30 rounded-xl relative overflow-hidden flex items-end">
        <div className={`w-full rounded-xl transition-all duration-500 ${color}`} style={{ height: `${Math.max(pct, 8)}%` }} />
      </div>
      <span className="text-sm font-bold text-on-surface">{value}</span>
      <span className="text-[10px] text-on-surface-variant">{unit}</span>
      <span className="text-[10px] text-on-surface-variant">{label}</span>
    </div>
  );
}

// --- Cooking Mode Overlay ---
function CookingMode({ steps, onClose, onComplete }: { steps: CookingStep[]; onClose: () => void; onComplete: () => void }) {
  const [currentStep, setCurrentStep] = useState(0);
  const step = steps[currentStep];

  return (
    <div className="fixed inset-0 z-50 bg-surface flex flex-col">
      {/* Header */}
      <div className="flex items-center justify-between px-4 pt-12 pb-3">
        <button onClick={onClose} className="p-2 rounded-full hover:bg-surface-variant">
          <X size={22} className="text-on-surface" />
        </button>
        <span className="text-sm font-semibold text-on-surface">
          {currentStep + 1} / {steps.length}
        </span>
        <div className="w-10" />
      </div>

      {/* Progress */}
      <div className="px-4 flex gap-1">
        {steps.map((_, i) => (
          <div key={i} className="flex-1 h-1 rounded-full overflow-hidden bg-outline-variant">
            <div className={`h-full rounded-full transition-all duration-300 ${i <= currentStep ? 'bg-primary' : ''}`}
              style={{ width: i <= currentStep ? '100%' : '0%' }}
            />
          </div>
        ))}
      </div>

      {/* Step Content */}
      <div className="flex-1 flex flex-col justify-center px-6 py-8">
        <div className="w-16 h-16 rounded-full bg-primary flex items-center justify-center mx-auto mb-6">
          <span className="text-2xl font-bold text-white">{step.order}</span>
        </div>
        <p className="text-lg text-on-surface leading-relaxed text-center">
          {step.description}
        </p>
        {step.timerSeconds && step.timerSeconds > 0 && (
          <div className="mt-6 max-w-xs mx-auto w-full">
            <StepTimer seconds={step.timerSeconds} />
          </div>
        )}
      </div>

      {/* Navigation */}
      <div className="px-4 pb-24 flex gap-3">
        <button
          onClick={() => setCurrentStep((p) => Math.max(0, p - 1))}
          disabled={currentStep === 0}
          className="flex-1 h-12 rounded-xl border border-outline flex items-center justify-center gap-1 text-sm font-medium text-on-surface disabled:opacity-30"
        >
          <ChevronLeft size={18} /> 이전
        </button>
        {currentStep < steps.length - 1 ? (
          <button
            onClick={() => setCurrentStep((p) => p + 1)}
            className="flex-1 h-12 rounded-xl bg-primary text-white flex items-center justify-center gap-1 text-sm font-medium active:scale-[0.97] transition-transform"
          >
            다음 <ChevronRight size={18} />
          </button>
        ) : (
          <button
            onClick={() => { onComplete(); onClose(); }}
            className="flex-1 h-12 rounded-xl bg-safe text-white flex items-center justify-center gap-1 text-sm font-medium active:scale-[0.97] transition-transform"
          >
            완료!
          </button>
        )}
      </div>
    </div>
  );
}

// --- Main Page ---
export default function RecipeDetailPage({ params }: PageProps) {
  const { id } = use(params);
  const router = useRouter();
  const [recipe, setRecipe] = useState<RecipeDetail | null>(null);
  const [loading, setLoading] = useState(true);
  const [bookmarked, setBookmarked] = useState(false);
  const [bookmarkLoading, setBookmarkLoading] = useState(false);
  const [cookingMode, setCookingMode] = useState(false);
  const [liked, setLiked] = useState(false);
  const [likeCount, setLikeCount] = useState(0);
  const [shareToast, setShareToast] = useState(false);

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
        setLiked(res.data.isLiked || false);
        setLikeCount(res.data.likeCount || 0);
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

  async function handleLike() {
    try {
      if (liked) {
        await api.unlikeRecipe(Number(id));
        setLikeCount(prev => Math.max(0, prev - 1));
      } else {
        await api.likeRecipe(Number(id));
        setLikeCount(prev => prev + 1);
      }
      setLiked(prev => !prev);
    } catch {}
  }

  async function handleShare() {
    const shareData = { title: recipe!.title, text: recipe!.description || '', url: window.location.href };
    try {
      if (navigator.share) {
        await navigator.share(shareData);
      } else {
        await navigator.clipboard.writeText(window.location.href);
        setShareToast(true);
        setTimeout(() => setShareToast(false), 2000);
      }
    } catch {}
  }

  async function handleCookingComplete() {
    try {
      await api.addCookingHistory({ recipeId: Number(id) });
    } catch {}
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

  const hasTimers = recipe.steps?.some((s) => s.timerSeconds && s.timerSeconds > 0);

  return (
    <div className="min-h-screen bg-surface pb-8">
      {/* Cooking Mode Overlay */}
      {cookingMode && recipe.steps && recipe.steps.length > 0 && (
        <CookingMode steps={recipe.steps} onClose={() => setCookingMode(false)} onComplete={handleCookingComplete} />
      )}

      {/* Hero section */}
      <div className="relative h-56">
        <div className="absolute inset-0 bg-gradient-to-br from-primary-300 to-primary flex items-center justify-center">
          <span className="text-7xl">🍽️</span>
        </div>
        <div className="absolute inset-x-0 bottom-0 h-24 bg-gradient-to-t from-black/60 to-transparent" />
        <button
          onClick={() => router.back()}
          className="absolute top-4 left-4 w-12 h-12 rounded-full bg-black/30 backdrop-blur-sm flex items-center justify-center"
        >
          <ArrowLeft size={20} className="text-white" />
        </button>
        <button
          onClick={handleBookmark}
          disabled={bookmarkLoading}
          className="absolute top-4 right-4 w-12 h-12 rounded-full bg-black/30 backdrop-blur-sm flex items-center justify-center active:scale-[0.95] transition-transform"
          style={{ transitionDuration: 'var(--duration-medium)' }}
        >
          <Heart size={20} className={bookmarked ? "text-accent fill-accent" : "text-white"} />
        </button>
        <div className="absolute inset-x-0 bottom-0 px-4 pb-3">
          <h1 className="text-white font-bold text-lg leading-tight drop-shadow">{recipe.title}</h1>
        </div>
      </div>

      {/* Content */}
      <div className="px-4 pt-4 space-y-5">
        {/* Info row */}
        <div className="flex items-center gap-2 flex-wrap">
          {recipe.difficulty && (
            <div className="flex items-center gap-1.5 bg-surface-variant rounded-lg px-3 h-8">
              <ChefHat size={14} className="text-on-surface-variant" />
              <span className="text-xs text-on-surface-variant">{getDifficultyLabel(recipe.difficulty)}</span>
            </div>
          )}
          {recipe.cookingTime && (
            <div className="flex items-center gap-1.5 bg-surface-variant rounded-lg px-3 h-8">
              <Clock size={14} className="text-on-surface-variant" />
              <span className="text-xs text-on-surface-variant">{formatCookingTime(recipe.cookingTime)}</span>
            </div>
          )}
          <div className="flex items-center gap-1.5 bg-surface-variant rounded-lg px-3 h-8">
            <Users size={14} className="text-on-surface-variant" />
            <span className="text-xs text-on-surface-variant">{recipe.servings}인분</span>
          </div>
          {recipe.avgRating > 0 && (
            <div className="flex items-center gap-1.5 bg-surface-variant rounded-lg px-3 h-8">
              <Star size={14} className="text-secondary fill-secondary" />
              <span className="text-xs text-on-surface-variant">{recipe.avgRating.toFixed(1)}</span>
            </div>
          )}
        </div>

        {/* 좋아요 & 공유 */}
        <div className="flex items-center gap-3">
          <button onClick={handleLike} className="flex items-center gap-1.5 bg-surface-variant rounded-lg px-3 h-8 active:scale-95 transition-transform">
            <Heart size={14} className={liked ? "text-accent fill-accent" : "text-on-surface-variant"} />
            <span className="text-xs text-on-surface-variant">{likeCount}</span>
          </button>
          <button onClick={handleShare} className="flex items-center gap-1.5 bg-surface-variant rounded-lg px-3 h-8 active:scale-95 transition-transform">
            <Share2 size={14} className="text-on-surface-variant" />
            <span className="text-xs text-on-surface-variant">공유</span>
          </button>
        </div>

        {/* Tags */}
        {recipe.tags && recipe.tags.length > 0 && (
          <div className="flex gap-2 flex-wrap">
            {recipe.tags.map((tag) => (
              <span key={tag} className="text-xs px-3 h-8 inline-flex items-center rounded-lg border border-outline text-on-surface-variant">
                #{tag}
              </span>
            ))}
          </div>
        )}

        {/* Description */}
        {recipe.description && (
          <p className="text-sm text-on-surface-variant leading-relaxed">{recipe.description}</p>
        )}

        {/* Ingredients */}
        <section>
          <h2 className="text-base font-bold text-on-surface mb-3">재료</h2>
          <div className="bg-surface-variant rounded-2xl p-4 space-y-0">
            {recipe.ingredients.map((ing) => (
              <div key={ing.ingredientId} className="flex items-center justify-between py-2.5 border-b border-outline-variant last:border-0">
                <div className="flex items-center gap-2">
                  {ing.hasIngredient ? (
                    <span className="w-5 h-5 rounded-full bg-safe/15 flex items-center justify-center text-[10px] text-safe font-bold">✓</span>
                  ) : (
                    <span className="w-5 h-5 rounded-full bg-danger/15 flex items-center justify-center text-[10px] text-danger font-bold">✕</span>
                  )}
                  <span className="text-sm text-on-surface">{ing.name}</span>
                  {ing.isEssential && <Star size={12} className="text-secondary fill-secondary" />}
                </div>
                {ing.quantity && <span className="text-sm text-on-surface-variant">{ing.quantity}</span>}
              </div>
            ))}
          </div>
        </section>

        {/* Steps with timers */}
        {recipe.steps && recipe.steps.length > 0 && (
          <section>
            <div className="flex items-center justify-between mb-3">
              <h2 className="text-base font-bold text-on-surface">조리 순서</h2>
              {hasTimers && (
                <span className="text-xs text-primary bg-primary-50 px-2 py-1 rounded-lg flex items-center gap-1">
                  <Clock size={12} /> 타이머 포함
                </span>
              )}
            </div>
            <div className="space-y-3">
              {recipe.steps.map((step) => (
                <div key={step.order} className="bg-surface-variant rounded-xl p-3">
                  <div className="flex gap-3">
                    <div className="w-7 h-7 rounded-full bg-primary flex items-center justify-center shrink-0 mt-0.5">
                      <span className="text-xs font-bold text-white">{step.order}</span>
                    </div>
                    <p className="text-sm text-on-surface leading-relaxed flex-1">{step.description}</p>
                  </div>
                  {step.timerSeconds && step.timerSeconds > 0 && (
                    <div className="ml-10">
                      <StepTimer seconds={step.timerSeconds} />
                    </div>
                  )}
                </div>
              ))}
            </div>
          </section>
        )}

        {/* Nutrition with visual bars */}
        {recipe.nutrition && (
          <section>
            <h2 className="text-base font-bold text-on-surface mb-3">영양 정보</h2>
            <div className="bg-surface-variant rounded-2xl p-4">
              {/* Calorie highlight */}
              <div className="text-center mb-4">
                <span className="text-3xl font-bold text-primary">{recipe.nutrition.calories}</span>
                <span className="text-sm text-on-surface-variant ml-1">kcal</span>
              </div>
              {/* Macro bars */}
              <div className="flex gap-3">
                <NutritionBar label="단백질" value={recipe.nutrition.protein} unit="g" max={50} color="bg-blue-400" />
                <NutritionBar label="지방" value={recipe.nutrition.fat} unit="g" max={65} color="bg-amber-400" />
                <NutritionBar label="탄수화물" value={recipe.nutrition.carbs} unit="g" max={300} color="bg-emerald-400" />
              </div>
            </div>
          </section>
        )}

        {/* CTA Button */}
        <button
          onClick={() => setCookingMode(true)}
          disabled={!recipe.steps || recipe.steps.length === 0}
          className="w-full h-12 bg-primary text-white rounded-xl font-medium text-sm active:bg-primary-600 active:scale-[0.97] transition-all disabled:opacity-40 flex items-center justify-center gap-2"
          style={{ transitionDuration: 'var(--duration-fast)' }}
        >
          <Play size={16} /> 요리 시작하기
        </button>
      </div>

      {shareToast && (
        <div className="fixed bottom-24 left-1/2 -translate-x-1/2 bg-on-surface text-surface px-4 py-2 rounded-lg text-sm z-50">
          링크가 복사되었습니다
        </div>
      )}
    </div>
  );
}
