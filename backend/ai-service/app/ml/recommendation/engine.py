"""레시피 추천 엔진 - 콘텐츠 기반 필터링"""

from datetime import date, datetime


class RecommendationEngine:
    """
    총점 = 0.40 × 재료매칭률
         + 0.25 × 소비기한임박활용도
         + 0.15 × 사용자선호도
         + 0.10 × 인기도
         - 0.10 × 부족재료패널티
    """

    WEIGHT_MATCH = 0.40
    WEIGHT_EXPIRY = 0.25
    WEIGHT_PREFERENCE = 0.15
    WEIGHT_POPULARITY = 0.10
    WEIGHT_PENALTY = 0.10

    def _parse_date(self, date_str: str | None) -> date | None:
        if not date_str:
            return None
        try:
            return datetime.strptime(date_str[:10], "%Y-%m-%d").date()
        except ValueError:
            return None

    def _build_available_set(self, user_ingredients: list[dict]) -> set[int]:
        """만료되지 않은 재료 ID 집합 반환"""
        today = date.today()
        available = set()
        for item in user_ingredients:
            expiry = self._parse_date(item.get("expiry_date"))
            if expiry is not None and expiry < today:
                continue
            available.add(item["ingredient_id"])
        return available

    def _expiry_score(self, recipe: dict, user_ingredients: list[dict]) -> float:
        """소비기한 임박 재료 활용도 점수"""
        today = date.today()
        recipe_ingredient_ids = {ing["id"] for ing in recipe.get("ingredients", [])}

        urgent_count = 0
        near_count = 0

        for item in user_ingredients:
            if item["ingredient_id"] not in recipe_ingredient_ids:
                continue
            expiry = self._parse_date(item.get("expiry_date"))
            if expiry is None:
                continue
            days_left = (expiry - today).days
            if days_left <= 1:
                urgent_count += 1
            elif days_left <= 3:
                near_count += 1

        if urgent_count == 0 and near_count == 0:
            return 0.0

        score = (urgent_count * 1.0 + near_count * 0.7) / max(urgent_count + near_count, 1)
        return min(score, 1.0)

    def _popularity_score(self, recipe: dict, max_view_count: int) -> float:
        view_count = recipe.get("view_count", 0)
        avg_rating = recipe.get("avg_rating", 0.0)

        normalized_views = view_count / max_view_count if max_view_count > 0 else 0.0
        normalized_rating = avg_rating / 5.0

        return (normalized_views + normalized_rating) / 2.0

    def _match_label(self, match_ratio: float) -> str:
        if match_ratio >= 0.80:
            return "바로 요리 가능"
        elif match_ratio >= 0.50:
            return "재료 조금 부족"
        elif match_ratio >= 0.30:
            return "도전해보세요"
        return "재료 부족"

    def calculate_score(
        self,
        recipe: dict,
        user_ingredients: list[dict],
        user_prefs: dict | None = None,
        max_view_count: int = 1,
    ) -> dict:
        """
        단일 레시피에 대한 추천 스코어 계산

        Returns: {
            recipe_id, total_score, match_ratio, matched_count, total_required,
            missing_ingredients, match_label, expiry_score, preference_score,
            popularity_score, penalty_score
        }
        """
        available = self._build_available_set(user_ingredients)

        essential_ingredients = [
            ing for ing in recipe.get("ingredients", []) if ing.get("is_essential", True)
        ]
        total_required = len(essential_ingredients)

        matched_ids = []
        missing_ingredient_ids = []

        for ing in essential_ingredients:
            ing_id = ing["id"]
            substitute_ids = ing.get("substitute_ids", [])
            all_options = {ing_id} | set(substitute_ids)
            if all_options & available:
                matched_ids.append(ing_id)
            else:
                missing_ingredient_ids.append(ing_id)

        matched_count = len(matched_ids)
        match_ratio = matched_count / total_required if total_required > 0 else 0.0

        expiry_score = self._expiry_score(recipe, user_ingredients)
        preference_score = 0.5  # Cold Start 기본값
        popularity_score = self._popularity_score(recipe, max_view_count)

        missing_count = len(missing_ingredient_ids)
        penalty_score = missing_count / total_required if total_required > 0 else 0.0

        total_score = (
            self.WEIGHT_MATCH * match_ratio
            + self.WEIGHT_EXPIRY * expiry_score
            + self.WEIGHT_PREFERENCE * preference_score
            - self.WEIGHT_PENALTY * penalty_score
            + self.WEIGHT_POPULARITY * popularity_score
        )

        return {
            "recipe_id": recipe["id"],
            "total_score": round(total_score, 4),
            "match_ratio": round(match_ratio, 4),
            "matched_count": matched_count,
            "total_required": total_required,
            "missing_ingredients": missing_ingredient_ids,
            "match_label": self._match_label(match_ratio),
            "expiry_score": round(expiry_score, 4),
            "preference_score": round(preference_score, 4),
            "popularity_score": round(popularity_score, 4),
            "penalty_score": round(penalty_score, 4),
        }

    def recommend(
        self,
        recipes: list[dict],
        user_ingredients: list[dict],
        user_prefs: dict | None = None,
        limit: int = 20,
    ) -> list[dict]:
        """
        전체 레시피에 대해 추천 스코어 계산 후 정렬
        - 필수 재료 1개 이상 보유 AND 매칭률 30% 이상만 표시
        - 스코어 내림차순 정렬
        """
        available = self._build_available_set(user_ingredients)
        max_view_count = max((r.get("view_count", 0) for r in recipes), default=1) or 1

        results = []
        for recipe in recipes:
            score_data = self.calculate_score(
                recipe, user_ingredients, user_prefs, max_view_count
            )

            essential_ids = {
                ing["id"]
                for ing in recipe.get("ingredients", [])
                if ing.get("is_essential", True)
            }
            has_at_least_one = bool(essential_ids & available)

            if has_at_least_one and score_data["match_ratio"] >= 0.30:
                results.append(score_data)

        results.sort(key=lambda x: x["total_score"], reverse=True)
        return results[:limit]
