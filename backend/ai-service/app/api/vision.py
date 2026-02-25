import logging

from fastapi import APIRouter
from pydantic import BaseModel

router = APIRouter()
logger = logging.getLogger(__name__)


class VisionScanRequest(BaseModel):
    image_base64: str
    scan_id: int
    user_id: int


class VisionItem(BaseModel):
    name: str
    confidence: float = 0.0
    category: str | None = None


class VisionScanResponse(BaseModel):
    scan_id: int
    status: str = "processing"
    items: list[VisionItem] = []


@router.post("/ingredients", response_model=VisionScanResponse)
async def recognize_ingredients(request: VisionScanRequest):
    """식재료 사진 인식 요청을 받아 GPT-4o Vision으로 처리합니다."""
    logger.info(f"Vision scan requested: scan_id={request.scan_id}, user_id={request.user_id}")

    # TODO: Dispatch to Celery task or process directly with OpenAI Vision
    # from app.ml.vision.openai_vision import recognize_ingredients_from_image
    # result = await recognize_ingredients_from_image(request.image_base64)

    return VisionScanResponse(
        scan_id=request.scan_id,
        status="processing",
    )
