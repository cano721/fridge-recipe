"""추천 엔진 테스트"""

from datetime import date, timedelta

import pytest
from fastapi.testclient import TestClient

from app.main import app
from app.ml.recommendation.engine import RecommendationEngine

client = TestClient(app)


def future_date(days: int) -> str:
    return (date.today() + timedelta(days=days)).isoformat()


def past_date(days: int) -> str:
    return (date.today() - timedelta(days=days)).isoformat()


class TestRecommendationEngine:
    def setup_method(self):
        self.engine = RecommendationEngine()

    def test_perfect_match_high_score(self):
        """모든 재료를 보유한 경우 높은 스코어"""
        recipe = {
            "id": 1,
            "title": "된장찌개",
            "ingredients": [
                {"id": 1, "is_essential": True, "substitute_ids": []},
                {"id": 2, "is_essential": True, "substitute_ids": []},
            ],
            "view_count": 100,
            "avg_rating": 4.5,
        }
        user_ingredients = [
            {"ingredient_id": 1, "expiry_date": future_date(10)},
            {"ingredient_id": 2, "expiry_date": future_date(10)},
        ]
        result = self.engine.calculate_score(recipe, user_ingredients)
        assert result["match_ratio"] == 1.0
        assert result["match_label"] == "바로 요리 가능"
        assert result["matched_count"] == 2
        assert result["total_required"] == 2
        assert result["missing_ingredients"] == []

    def test_no_match_excluded(self):
        """매칭률 30% 미만이면 추천 목록에서 제외"""
        recipes = [
            {
                "id": 1,
                "title": "비빔밥",
                "ingredients": [
                    {"id": 10, "is_essential": True, "substitute_ids": []},
                    {"id": 11, "is_essential": True, "substitute_ids": []},
                    {"id": 12, "is_essential": True, "substitute_ids": []},
                    {"id": 13, "is_essential": True, "substitute_ids": []},
                ],
                "view_count": 50,
                "avg_rating": 4.0,
            }
        ]
        # 4개 중 1개만 보유 → 25% → 제외
        user_ingredients = [{"ingredient_id": 10, "expiry_date": future_date(5)}]
        results = self.engine.recommend(recipes, user_ingredients)
        assert results == []

    def test_substitute_ingredient_counts(self):
        """대체 재료 보유 시 매칭으로 간주"""
        recipe = {
            "id": 2,
            "title": "카레",
            "ingredients": [
                {"id": 1, "is_essential": True, "substitute_ids": [99]},  # 99를 보유
                {"id": 2, "is_essential": True, "substitute_ids": []},
            ],
            "view_count": 0,
            "avg_rating": 0.0,
        }
        user_ingredients = [
            {"ingredient_id": 99, "expiry_date": future_date(5)},  # 대체 재료
            {"ingredient_id": 2, "expiry_date": future_date(5)},
        ]
        result = self.engine.calculate_score(recipe, user_ingredients)
        assert result["match_ratio"] == 1.0
        assert result["matched_count"] == 2
        assert 1 not in result["missing_ingredients"]

    def test_expiry_urgent_bonus(self):
        """소비기한 임박 재료(D-1) 사용 시 expiry_score 최대"""
        recipe = {
            "id": 3,
            "title": "볶음밥",
            "ingredients": [
                {"id": 1, "is_essential": True, "substitute_ids": []},
            ],
            "view_count": 0,
            "avg_rating": 0.0,
        }
        user_ingredients_urgent = [
            {"ingredient_id": 1, "expiry_date": future_date(0)},  # 오늘 만료 (D-0)
        ]
        user_ingredients_safe = [
            {"ingredient_id": 1, "expiry_date": future_date(30)},
        ]
        result_urgent = self.engine.calculate_score(recipe, user_ingredients_urgent)
        result_safe = self.engine.calculate_score(recipe, user_ingredients_safe)
        assert result_urgent["expiry_score"] == 1.0
        assert result_safe["expiry_score"] == 0.0
        assert result_urgent["total_score"] > result_safe["total_score"]

    def test_expiry_near_bonus(self):
        """소비기한 D-2~D-3 재료는 expiry_score 0.7"""
        recipe = {
            "id": 4,
            "title": "파스타",
            "ingredients": [
                {"id": 1, "is_essential": True, "substitute_ids": []},
            ],
            "view_count": 0,
            "avg_rating": 0.0,
        }
        user_ingredients = [{"ingredient_id": 1, "expiry_date": future_date(2)}]
        result = self.engine.calculate_score(recipe, user_ingredients)
        assert result["expiry_score"] == 0.7

    def test_expired_ingredient_excluded(self):
        """만료된 재료는 보유 재료로 인정하지 않음"""
        recipe = {
            "id": 5,
            "title": "계란찜",
            "ingredients": [
                {"id": 1, "is_essential": True, "substitute_ids": []},
            ],
            "view_count": 0,
            "avg_rating": 0.0,
        }
        user_ingredients = [{"ingredient_id": 1, "expiry_date": past_date(1)}]
        result = self.engine.calculate_score(recipe, user_ingredients)
        assert result["match_ratio"] == 0.0
        assert 1 in result["missing_ingredients"]

    def test_match_labels(self):
        """매칭률에 따른 라벨 분류"""
        assert self.engine._match_label(1.0) == "바로 요리 가능"
        assert self.engine._match_label(0.80) == "바로 요리 가능"
        assert self.engine._match_label(0.79) == "재료 조금 부족"
        assert self.engine._match_label(0.50) == "재료 조금 부족"
        assert self.engine._match_label(0.49) == "도전해보세요"
        assert self.engine._match_label(0.30) == "도전해보세요"
        assert self.engine._match_label(0.29) == "재료 부족"

    def test_recommend_sorted_by_score(self):
        """추천 결과가 스코어 내림차순 정렬"""
        recipes = [
            {
                "id": 1,
                "title": "저매칭",
                "ingredients": [
                    {"id": 1, "is_essential": True, "substitute_ids": []},
                    {"id": 2, "is_essential": True, "substitute_ids": []},
                ],
                "view_count": 0,
                "avg_rating": 0.0,
            },
            {
                "id": 2,
                "title": "고매칭",
                "ingredients": [
                    {"id": 1, "is_essential": True, "substitute_ids": []},
                ],
                "view_count": 0,
                "avg_rating": 0.0,
            },
        ]
        user_ingredients = [{"ingredient_id": 1, "expiry_date": future_date(10)}]
        results = self.engine.recommend(recipes, user_ingredients)
        assert len(results) == 2
        assert results[0]["total_score"] >= results[1]["total_score"]

    def test_recommend_limit(self):
        """limit 파라미터 동작"""
        recipes = [
            {
                "id": i,
                "title": f"레시피{i}",
                "ingredients": [{"id": 1, "is_essential": True, "substitute_ids": []}],
                "view_count": 0,
                "avg_rating": 0.0,
            }
            for i in range(10)
        ]
        user_ingredients = [{"ingredient_id": 1, "expiry_date": future_date(10)}]
        results = self.engine.recommend(recipes, user_ingredients, limit=3)
        assert len(results) == 3


def test_recommend_api_endpoint():
    """POST /ai/recommend 엔드포인트 테스트"""
    payload = {
        "recipes": [
            {
                "id": 1,
                "title": "된장찌개",
                "ingredients": [
                    {"id": 1, "name": "된장", "is_essential": True, "substitute_ids": []},
                    {"id": 2, "name": "두부", "is_essential": True, "substitute_ids": []},
                ],
                "view_count": 50,
                "avg_rating": 4.2,
            }
        ],
        "user_ingredients": [
            {"ingredient_id": 1, "expiry_date": future_date(5), "storage_type": "fridge"},
            {"ingredient_id": 2, "expiry_date": future_date(3), "storage_type": "fridge"},
        ],
        "limit": 10,
    }
    response = client.post(
        "/ai/recommend",
        json=payload,
        headers={"X-Internal-Api-Key": "dev-internal-key"},
    )
    assert response.status_code == 200
    data = response.json()
    assert "recommendations" in data
    assert len(data["recommendations"]) == 1
    rec = data["recommendations"][0]
    assert rec["recipe_id"] == 1
    assert rec["match_ratio"] == 1.0
    assert rec["match_label"] == "바로 요리 가능"


def test_recommend_api_requires_api_key():
    """API 키 없이 접근 시 403"""
    response = client.post("/ai/recommend", json={})
    assert response.status_code == 403
