import logging

from fastapi import APIRouter
from pydantic import BaseModel

router = APIRouter()
logger = logging.getLogger(__name__)


class RecommendRequest(BaseModel):
    user_id: int
    ingredient_ids: list[int]
    expiring_ingredient_ids: list[int] = []
    dietary_prefs: dict = {}
    limit: int = 20


class RecommendedRecipe(BaseModel):
    recipe_id: int
    title: str
    match_ratio: float
    matched_count: int
    total_required: int
    missing_ingredients: list[str] = []
    score: float = 0.0


class RecommendResponse(BaseModel):
    recommendations: list[RecommendedRecipe] = []


@router.post("", response_model=RecommendResponse)
async def get_recommendations(request: RecommendRequest):
    """보유 재료 기반 레시피 추천을 수행합니다.

    스코어링 공식:
    총점 = 0.40 × 재료매칭률
         + 0.25 × 유통기한임박활용도
         + 0.15 × 사용자선호도
         + 0.10 × 인기도
         - 0.10 × 부족재료패널티
    """
    logger.info(f"Recommendation requested: user_id={request.user_id}, ingredients={len(request.ingredient_ids)}")

    # TODO: Implement recommendation engine
    # from app.ml.recommendation.engine import RecommendationEngine
    # engine = RecommendationEngine()
    # results = engine.recommend(request)

    return RecommendResponse(recommendations=[])
