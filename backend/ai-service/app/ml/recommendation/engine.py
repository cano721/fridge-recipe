"""콘텐츠 기반 레시피 추천 엔진.

스코어링 공식:
총점 = 0.40 × 재료매칭률
     + 0.25 × 유통기한임박활용도
     + 0.15 × 사용자선호도
     + 0.10 × 인기도
     - 0.10 × 부족재료패널티
"""

import logging

logger = logging.getLogger(__name__)

# Score weights
W_MATCH = 0.40
W_EXPIRY = 0.25
W_PREFERENCE = 0.15
W_POPULARITY = 0.10
W_MISSING_PENALTY = 0.10


class RecommendationEngine:
    """콘텐츠 기반 필터링 추천 엔진."""

    def calculate_match_ratio(
        self,
        recipe_ingredient_ids: list[int],
        user_ingredient_ids: set[int],
    ) -> tuple[float, int, int, list[int]]:
        """재료 매칭률을 계산합니다.

        Returns:
            (match_ratio, matched_count, total_required, missing_ids)
        """
        total = len(recipe_ingredient_ids)
        if total == 0:
            return 0.0, 0, 0, []

        matched = [iid for iid in recipe_ingredient_ids if iid in user_ingredient_ids]
        missing = [iid for iid in recipe_ingredient_ids if iid not in user_ingredient_ids]

        return len(matched) / total, len(matched), total, missing

    def calculate_expiry_score(
        self,
        recipe_ingredient_ids: list[int],
        expiring_ingredient_ids: set[int],
    ) -> float:
        """유통기한 임박 재료 활용도를 계산합니다."""
        if not expiring_ingredient_ids:
            return 0.0

        used_expiring = sum(1 for iid in recipe_ingredient_ids if iid in expiring_ingredient_ids)
        return min(used_expiring / max(len(expiring_ingredient_ids), 1), 1.0)

    def calculate_score(
        self,
        match_ratio: float,
        expiry_score: float,
        preference_score: float = 0.5,
        popularity_score: float = 0.5,
        missing_count: int = 0,
    ) -> float:
        """최종 추천 스코어를 계산합니다."""
        missing_penalty = min(missing_count * 0.1, 1.0)

        score = (
            W_MATCH * match_ratio
            + W_EXPIRY * expiry_score
            + W_PREFERENCE * preference_score
            + W_POPULARITY * popularity_score
            - W_MISSING_PENALTY * missing_penalty
        )

        return max(score, 0.0)

    def recommend(
        self,
        recipes: list[dict],
        user_ingredient_ids: set[int],
        expiring_ingredient_ids: set[int] | None = None,
        min_match_ratio: float = 0.5,
        limit: int = 20,
    ) -> list[dict]:
        """레시피 목록에서 추천 결과를 생성합니다."""
        expiring = expiring_ingredient_ids or set()
        results = []

        for recipe in recipes:
            recipe_ingredients = recipe.get("ingredient_ids", [])
            match_ratio, matched, total, missing = self.calculate_match_ratio(
                recipe_ingredients, user_ingredient_ids
            )

            if match_ratio < min_match_ratio:
                continue

            expiry_score = self.calculate_expiry_score(recipe_ingredients, expiring)

            # Popularity score based on view count and rating
            popularity = min((recipe.get("view_count", 0) / 1000) * 0.5 + (recipe.get("avg_rating", 0) / 5) * 0.5, 1.0)

            score = self.calculate_score(
                match_ratio=match_ratio,
                expiry_score=expiry_score,
                popularity_score=popularity,
                missing_count=len(missing),
            )

            results.append(
                {
                    "recipe_id": recipe["id"],
                    "title": recipe["title"],
                    "match_ratio": round(match_ratio, 2),
                    "matched_count": matched,
                    "total_required": total,
                    "missing_ingredient_ids": missing,
                    "score": round(score, 4),
                }
            )

        results.sort(key=lambda x: x["score"], reverse=True)
        return results[:limit]
