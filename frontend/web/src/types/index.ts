export interface User {
  id: number;
  email: string | null;
  nickname: string;
  profileImage: string | null;
  oauthProvider: string;
  dietaryPrefs: Record<string, unknown>;
}

export interface Ingredient {
  id: number;
  ingredientId: number;
  name: string;
  category: string;
  quantity: number | null;
  unit: string | null;
  expiryDate: string | null;
  storageType: string;
  memo: string | null;
  registeredVia: string;
  daysUntilExpiry: number | null;
}

export interface IngredientMaster {
  id: number;
  name: string;
  category: string;
  iconUrl: string | null;
  defaultUnit: string | null;
  defaultExpiryDays: number | null;
}

export interface Recipe {
  id: number;
  title: string;
  description: string | null;
  cuisineType: string | null;
  difficulty: string | null;
  cookingTime: number | null;
  servings: number;
  thumbnailUrl: string | null;
  tags: string[];
  viewCount: number;
  avgRating: number;
}

export interface RecipeDetail extends Recipe {
  steps: CookingStep[];
  nutrition: NutritionInfo | null;
  ingredients: RecipeIngredient[];
}

export interface CookingStep {
  order: number;
  description: string;
  imageUrl?: string;
  timerSeconds?: number;
}

export interface NutritionInfo {
  calories: number;
  protein: number;
  fat: number;
  carbs: number;
}

export interface RecipeIngredient {
  ingredientId: number;
  name: string;
  quantity: string | null;
  isEssential: boolean;
  hasIngredient?: boolean;
}

export interface RecipeRecommendation {
  recipe: Recipe;
  matchRatio: number;
  matchedIngredients: string[];
  missingIngredients: string[];
  matchLabel: string;
}

export interface CookingHistory {
  id: number;
  recipeId: number;
  recipeTitle: string;
  recipeThumbnailUrl: string | null;
  cookedAt: string;
  rating: number | null;
  memo: string | null;
}

export interface RecipeLike {
  recipeId: number;
  createdAt: string;
}

export type StorageType = 'fridge' | 'freezer' | 'room';
export type ExpiryStatus = 'safe' | 'soon' | 'urgent' | 'expired';
export type Difficulty = 'easy' | 'medium' | 'hard';
export type RecipeSortBy = 'recommended' | 'rating' | 'cookingTime' | 'newest';

export interface ApiResponse<T> {
  success: boolean;
  data?: T;
  error?: { code: string; message: string };
}
