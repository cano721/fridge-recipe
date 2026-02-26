from fastapi import APIRouter
from pydantic import BaseModel

from app.ml.recommendation.engine import RecommendationEngine

router = APIRouter(tags=["Recommendation"])


class RecipeData(BaseModel):
    id: int
    title: str
    ingredients: list[dict]  # [{id, name, is_essential, substitute_ids}]
    view_count: int = 0
    avg_rating: float = 0.0


class UserIngredientData(BaseModel):
    ingredient_id: int
    expiry_date: str | None = None  # ISO date
    storage_type: str = "fridge"


class RecommendRequest(BaseModel):
    recipes: list[RecipeData]
    user_ingredients: list[UserIngredientData]
    user_prefs: dict | None = None
    limit: int = 20


class RecommendResponse(BaseModel):
    recommendations: list[dict]


@router.post("/recommend", response_model=RecommendResponse)
async def get_recommendations(request: RecommendRequest):
    """보유 재료 기반 레시피 추천"""
    engine = RecommendationEngine()
    recipes_data = [r.model_dump() for r in request.recipes]
    ingredients_data = [i.model_dump() for i in request.user_ingredients]

    results = engine.recommend(
        recipes=recipes_data,
        user_ingredients=ingredients_data,
        user_prefs=request.user_prefs,
        limit=request.limit,
    )
    return RecommendResponse(recommendations=results)
