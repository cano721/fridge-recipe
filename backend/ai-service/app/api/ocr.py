from fastapi import APIRouter
from pydantic import BaseModel

from app.core.celery_app import celery_app
from app.tasks.ocr_tasks import receipt_ocr_task

router = APIRouter(tags=["OCR"])


class OcrRequest(BaseModel):
    image_url: str
    user_id: int


class OcrResponse(BaseModel):
    task_id: str
    status: str = "processing"


class OcrResultResponse(BaseModel):
    task_id: str
    status: str
    items: list[dict] | None = None
    error: str | None = None


@router.post("/ocr/receipt", response_model=OcrResponse, status_code=202)
async def scan_receipt(request: OcrRequest):
    """영수증 OCR 스캔 요청"""
    task = receipt_ocr_task.delay(request.image_url, request.user_id)
    return OcrResponse(task_id=task.id, status="processing")


@router.get("/ocr/receipt/{task_id}", response_model=OcrResultResponse)
async def get_scan_result(task_id: str):
    """스캔 결과 조회"""
    result = celery_app.AsyncResult(task_id)
    if result.ready():
        if result.successful():
            data = result.result
            return OcrResultResponse(task_id=task_id, status="done", items=data.get("items"))
        else:
            return OcrResultResponse(task_id=task_id, status="failed", error=str(result.result))
    return OcrResultResponse(task_id=task_id, status="processing")
