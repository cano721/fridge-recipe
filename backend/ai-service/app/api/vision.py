from fastapi import APIRouter
from pydantic import BaseModel

from app.core.celery_app import celery_app
from app.tasks.ocr_tasks import photo_vision_task

router = APIRouter(tags=["Vision"])


class VisionRequest(BaseModel):
    image_url: str
    user_id: int


class VisionResponse(BaseModel):
    task_id: str
    status: str = "processing"


class VisionResultResponse(BaseModel):
    task_id: str
    status: str
    ingredients: list[dict] | None = None
    error: str | None = None


@router.post("/vision/ingredients", response_model=VisionResponse, status_code=202)
async def recognize_ingredients(request: VisionRequest):
    """식재료 사진 인식 요청"""
    task = photo_vision_task.delay(request.image_url, request.user_id)
    return VisionResponse(task_id=task.id, status="processing")


@router.get("/vision/ingredients/{task_id}", response_model=VisionResultResponse)
async def get_recognition_result(task_id: str):
    """인식 결과 조회"""
    result = celery_app.AsyncResult(task_id)
    if result.ready():
        if result.successful():
            data = result.result
            return VisionResultResponse(task_id=task_id, status="done", ingredients=data.get("ingredients"))
        else:
            return VisionResultResponse(task_id=task_id, status="failed", error=str(result.result))
    return VisionResultResponse(task_id=task_id, status="processing")
