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

  // Recipes
  getRecommendations(limit?: number) {
    const query = limit ? `?limit=${limit}` : '';
    return this.request<any>(`/recipes/recommend${query}`);
  }

  getRecipeDetail(id: number) {
    return this.request<any>(`/recipes/${id}`);
  }

  searchRecipes(query: string, cuisineType?: string) {
    let path = `/recipes/search?q=${encodeURIComponent(query)}`;
    if (cuisineType) path += `&cuisineType=${cuisineType}`;
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
}

export const api = new ApiClient();
