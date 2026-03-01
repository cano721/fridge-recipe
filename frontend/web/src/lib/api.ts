const API_BASE = '/api/v1';

class ApiClient {
  private token: string | null = null;
  private refreshPromise: Promise<boolean> | null = null;

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

  private async refreshTokens(): Promise<boolean> {
    if (typeof window === 'undefined') return false;

    try {
      // refreshToken은 httpOnly 쿠키로 자동 전송됨
      const response = await fetch(`${API_BASE}/auth/refresh`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
      });

      if (!response.ok) return false;

      const data = await response.json();
      if (data.success && data.data) {
        this.setToken(data.data.accessToken);
        return true;
      }
      return false;
    } catch {
      return false;
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

    // 401 감지: refresh 요청 자체는 재시도하지 않음 (무한루프 방지)
    if (response.status === 401 && !path.includes('/auth/refresh')) {
      // 동시 다발적 401에 대한 중복 refresh 방지 (Promise 공유)
      if (!this.refreshPromise) {
        this.refreshPromise = this.refreshTokens().finally(() => {
          this.refreshPromise = null;
        });
      }

      const refreshed = await this.refreshPromise;
      if (refreshed) {
        // 새 토큰으로 원래 요청 재시도
        const retryHeaders: Record<string, string> = {
          ...headers,
          'Authorization': `Bearer ${this.getToken()}`,
        };
        const retryResponse = await fetch(`${API_BASE}${path}`, { ...options, headers: retryHeaders });
        if (!retryResponse.ok) {
          const error = await retryResponse.json().catch(() => ({}));
          return { success: false, error: error?.error || { code: String(retryResponse.status), message: retryResponse.statusText } } as T;
        }
        return retryResponse.json();
      }

      // refresh 실패: 토큰 클리어 + 로그인 리다이렉트
      this.clearToken();
      if (typeof window !== 'undefined') {
        window.location.href = '/login';
      }
      return { success: false, error: { code: '401', message: 'Session expired' } } as T;
    }

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

  updateIngredient(id: number, body: { quantity?: number; unit?: string; expiryDate?: string; storageType?: string; memo?: string }) {
    return this.request<any>(`/ingredients/${id}`, { method: 'PUT', body: JSON.stringify(body) });
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

  updateProfile(body: { nickname?: string; profileImage?: string }) {
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

  // Scan
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
