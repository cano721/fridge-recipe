const API_BASE = '/api/v1';

class ApiClient {
  private token: string | null = null;

  setToken(token: string) {
    this.token = token;
    if (typeof window !== 'undefined') {
      localStorage.setItem('token', token);
    }
  }

  getToken(): string | null {
    if (!this.token && typeof window !== 'undefined') {
      this.token = localStorage.getItem('token');
    }
    return this.token;
  }

  clearToken() {
    this.token = null;
    if (typeof window !== 'undefined') {
      localStorage.removeItem('token');
    }
  }

  private async request<T>(path: string, options: RequestInit = {}): Promise<T> {
    const headers: Record<string, string> = {
      'Content-Type': 'application/json',
      ...((options.headers as Record<string, string>) || {}),
    };

    const token = this.getToken();
    if (token) {
      headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(`${API_BASE}${path}`, {
      ...options,
      headers,
    });

    if (!response.ok) {
      const error = await response.json().catch(() => ({}));
      return { success: false, error: error?.error || { code: String(response.status), message: response.statusText } } as T;
    }

    const data = await response.json();
    return data;
  }

  // Health
  health() { return this.request<any>('/health'); }

  // Ingredients
  getIngredients(storageType?: string) {
    const query = storageType ? `?storageType=${storageType}` : '';
    return this.request<any>(`/ingredients${query}`);
  }

  addIngredient(body: { ingredientId: number; quantity?: number; unit?: string; expiryDate?: string; storageType?: string; memo?: string }) {
    return this.request<any>('/ingredients', { method: 'POST', body: JSON.stringify(body) });
  }

  deleteIngredient(id: number) {
    return this.request<any>(`/ingredients/${id}`, { method: 'DELETE' });
  }

  getCategories() {
    return this.request<any>('/ingredients/categories');
  }

  getExpiringItems(days?: number) {
    const query = days ? `?days=${days}` : '';
    return this.request<any>(`/ingredients/expiring${query}`);
  }

  searchIngredientMaster(query: string) {
    return this.request<any>(`/ingredients/search?q=${encodeURIComponent(query)}`);
  }

  // Auth
  login(provider: string, accessToken: string) {
    return this.request<any>('/auth/login', {
      method: 'POST',
      body: JSON.stringify({ provider, accessToken }),
    });
  }

  // Users
  getMe() {
    return this.request<any>('/users/me');
  }

  updateProfile(body: { nickname: string }) {
    return this.request<any>('/users/me', { method: 'PUT', body: JSON.stringify(body) });
  }

  // Recipes
  getRecommendations(limit?: number) {
    const query = limit ? `?limit=${limit}` : '';
    return this.request<any>(`/recipes/recommend${query}`);
  }

  getRecipeDetail(id: number) {
    return this.request<any>(`/recipes/${id}`);
  }

  searchRecipes(query: string, options?: { cuisineType?: string; difficulty?: string; maxCookingTime?: number; ingredientIds?: number[]; sort?: string }) {
    let path = `/recipes/search?q=${encodeURIComponent(query)}`;
    if (options?.cuisineType) path += `&cuisineType=${options.cuisineType}`;
    if (options?.difficulty) path += `&difficulty=${options.difficulty}`;
    if (options?.maxCookingTime) path += `&maxCookingTime=${options.maxCookingTime}`;
    if (options?.ingredientIds?.length) path += `&ingredientIds=${options.ingredientIds.join(',')}`;
    if (options?.sort) path += `&sort=${options.sort}`;
    return this.request<any>(path);
  }

  addBookmark(recipeId: number) {
    return this.request<any>(`/recipes/${recipeId}/bookmark`, { method: 'POST' });
  }

  removeBookmark(recipeId: number) {
    return this.request<any>(`/recipes/${recipeId}/bookmark`, { method: 'DELETE' });
  }

  getBookmarks() {
    return this.request<any>('/recipes/bookmarks');
  }

  // Likes
  likeRecipe(recipeId: number) {
    return this.request<any>(`/recipes/${recipeId}/like`, { method: 'POST' });
  }

  unlikeRecipe(recipeId: number) {
    return this.request<any>(`/recipes/${recipeId}/like`, { method: 'DELETE' });
  }

  // Cooking History
  getCookingHistory() {
    return this.request<any>('/users/me/history');
  }

  addCookingHistory(body: { recipeId: number; rating?: number; memo?: string }) {
    return this.request<any>('/users/me/history', { method: 'POST', body: JSON.stringify(body) });
  }

  // Preferences
  getPreferences() {
    return this.request<any>('/users/me/preferences');
  }

  updatePreferences(prefs: Record<string, unknown>) {
    return this.request<any>('/users/me/preferences', {
      method: 'PUT',
      body: JSON.stringify(prefs),
    });
  }

  // Notifications
  getNotifications() {
    return this.request<any>('/notifications');
  }

  // Scan — 즉시 결과 반환 (폴링 불필요)
  submitScan(type: 'receipt' | 'photo', imageBase64: string) {
    return this.request<any>('/scan', {
      method: 'POST',
      body: JSON.stringify({ type, image: imageBase64 }),
    });
  }

  /** @deprecated scan API가 즉시 결과를 반환하므로 더 이상 폴링 불필요 */
  getScanResult(taskId: string) {
    return this.request<any>(`/scan/result/${taskId}`);
  }
}

export const api = new ApiClient();
