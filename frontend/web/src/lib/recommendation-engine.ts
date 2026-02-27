/**
 * 레시피 추천 엔진 — 콘텐츠 기반 필터링
 *
 * 총점 = 0.40 × 재료매칭률
 *      + 0.25 × 소비기한임박활용도
 *      + 0.15 × 사용자선호도
 *      + 0.10 × 인기도
 *      - 0.10 × 부족재료패널티
 */

const WEIGHT_MATCH = 0.40;
const WEIGHT_EXPIRY = 0.25;
const WEIGHT_PREFERENCE = 0.15;
const WEIGHT_POPULARITY = 0.10;
const WEIGHT_PENALTY = 0.10;

export interface RecipeInput {
  id: number;
  title: string;
  description?: string | null;
  cuisine_type?: string | null;
  difficulty?: string | null;
  cooking_time?: number | null;
  servings?: number;
  thumbnail_url?: string | null;
  tags?: string[];
  avg_rating?: number;
  view_count?: number;
  ingredients: RecipeIngredientInput[];
}

export interface RecipeIngredientInput {
  id: number;
  name?: string;
  is_essential?: boolean;
  substitute_ids?: number[];
}

export interface UserIngredientInput {
  ingredient_id: number;
  expiry_date?: string | null;
  storage_type?: string;
}

export interface RecommendationResult {
  recipe_id: number;
  total_score: number;
  match_ratio: number;
  matched_count: number;
  total_required: number;
  missing_ingredients: number[];
  match_label: string;
  expiry_score: number;
  preference_score: number;
  popularity_score: number;
  penalty_score: number;
}

function parseDate(dateStr: string | null | undefined): Date | null {
  if (!dateStr) return null;
  const d = new Date(dateStr.slice(0, 10));
  return isNaN(d.getTime()) ? null : d;
}

function daysBetween(a: Date, b: Date): number {
  return Math.floor((b.getTime() - a.getTime()) / (1000 * 60 * 60 * 24));
}

function buildAvailableSet(userIngredients: UserIngredientInput[]): Set<number> {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  const available = new Set<number>();

  for (const item of userIngredients) {
    const expiry = parseDate(item.expiry_date);
    if (expiry !== null && expiry < today) continue;
    available.add(item.ingredient_id);
  }
  return available;
}

function expiryScore(recipe: RecipeInput, userIngredients: UserIngredientInput[]): number {
  const today = new Date();
  today.setHours(0, 0, 0, 0);
  const recipeIngredientIds = new Set(recipe.ingredients.map((ing) => ing.id));

  let urgentCount = 0;
  let nearCount = 0;

  for (const item of userIngredients) {
    if (!recipeIngredientIds.has(item.ingredient_id)) continue;
    const expiry = parseDate(item.expiry_date);
    if (expiry === null) continue;
    const daysLeft = daysBetween(today, expiry);
    if (daysLeft <= 1) {
      urgentCount++;
    } else if (daysLeft <= 3) {
      nearCount++;
    }
  }

  if (urgentCount === 0 && nearCount === 0) return 0.0;
  const score = (urgentCount * 1.0 + nearCount * 0.7) / Math.max(urgentCount + nearCount, 1);
  return Math.min(score, 1.0);
}

function popularityScore(recipe: RecipeInput, maxViewCount: number): number {
  const viewCount = recipe.view_count ?? 0;
  const avgRating = recipe.avg_rating ?? 0;

  const normalizedViews = maxViewCount > 0 ? viewCount / maxViewCount : 0;
  const normalizedRating = avgRating / 5.0;

  return (normalizedViews + normalizedRating) / 2.0;
}

function matchLabel(matchRatio: number): string {
  if (matchRatio >= 0.80) return '바로 요리 가능';
  if (matchRatio >= 0.50) return '재료 조금 부족';
  if (matchRatio >= 0.30) return '도전해보세요';
  return '재료 부족';
}

function round4(n: number): number {
  return Math.round(n * 10000) / 10000;
}

export function calculateScore(
  recipe: RecipeInput,
  userIngredients: UserIngredientInput[],
  maxViewCount: number = 1,
): RecommendationResult {
  const available = buildAvailableSet(userIngredients);

  const essentialIngredients = recipe.ingredients.filter(
    (ing) => ing.is_essential !== false,
  );
  const totalRequired = essentialIngredients.length;

  const matchedIds: number[] = [];
  const missingIngredientIds: number[] = [];

  for (const ing of essentialIngredients) {
    const allOptions = new Set([ing.id, ...(ing.substitute_ids ?? [])]);
    let found = false;
    for (const opt of allOptions) {
      if (available.has(opt)) { found = true; break; }
    }
    if (found) {
      matchedIds.push(ing.id);
    } else {
      missingIngredientIds.push(ing.id);
    }
  }

  const matchedCount = matchedIds.length;
  const matchRatio = totalRequired > 0 ? matchedCount / totalRequired : 0;

  const expiry = expiryScore(recipe, userIngredients);
  const preferenceScore = 0.5; // Cold Start 기본값
  const popularity = popularityScore(recipe, maxViewCount);

  const missingCount = missingIngredientIds.length;
  const penaltyScore = totalRequired > 0 ? missingCount / totalRequired : 0;

  const totalScore =
    WEIGHT_MATCH * matchRatio +
    WEIGHT_EXPIRY * expiry +
    WEIGHT_PREFERENCE * preferenceScore -
    WEIGHT_PENALTY * penaltyScore +
    WEIGHT_POPULARITY * popularity;

  return {
    recipe_id: recipe.id,
    total_score: round4(totalScore),
    match_ratio: round4(matchRatio),
    matched_count: matchedCount,
    total_required: totalRequired,
    missing_ingredients: missingIngredientIds,
    match_label: matchLabel(matchRatio),
    expiry_score: round4(expiry),
    preference_score: round4(preferenceScore),
    popularity_score: round4(popularity),
    penalty_score: round4(penaltyScore),
  };
}

export function recommend(
  recipes: RecipeInput[],
  userIngredients: UserIngredientInput[],
  limit: number = 20,
): RecommendationResult[] {
  const available = buildAvailableSet(userIngredients);
  const maxViewCount = Math.max(...recipes.map((r) => r.view_count ?? 0), 1);

  const results: RecommendationResult[] = [];

  for (const recipe of recipes) {
    const scoreData = calculateScore(recipe, userIngredients, maxViewCount);

    const essentialIds = new Set(
      recipe.ingredients
        .filter((ing) => ing.is_essential !== false)
        .map((ing) => ing.id),
    );

    let hasAtLeastOne = false;
    for (const id of essentialIds) {
      if (available.has(id)) { hasAtLeastOne = true; break; }
    }

    if (hasAtLeastOne && scoreData.match_ratio >= 0.30) {
      results.push(scoreData);
    }
  }

  results.sort((a, b) => b.total_score - a.total_score);
  return results.slice(0, limit);
}
