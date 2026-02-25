import logging

from fastapi import APIRouter
from pydantic import BaseModel

router = APIRouter()
logger = logging.getLogger(__name__)


class ReceiptScanRequest(BaseModel):
    image_base64: str
    scan_id: int
    user_id: int


class ScanItem(BaseModel):
    raw_text: str
    matched_ingredient_id: int | None = None
    matched_ingredient_name: str | None = None
    quantity: str | None = None
    confidence: str = "low"
    is_food: bool = True


class ReceiptScanResponse(BaseModel):
    scan_id: int
    status: str = "processing"
    items: list[ScanItem] = []


@router.post("/receipt", response_model=ReceiptScanResponse)
async def scan_receipt(request: ReceiptScanRequest):
    """영수증 OCR 스캔 요청을 받아 Celery 태스크로 처리합니다."""
    logger.info(f"Receipt scan requested: scan_id={request.scan_id}, user_id={request.user_id}")

    # TODO: Dispatch to Celery task for async processing
    # from app.tasks.ocr_tasks import process_receipt_ocr
    # process_receipt_ocr.delay(request.scan_id, request.image_base64)

    return ReceiptScanResponse(
        scan_id=request.scan_id,
        status="processing",
    )
