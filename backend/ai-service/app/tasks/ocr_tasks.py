"""Celery tasks for OCR processing."""

import logging

from app.core.celery_app import celery_app

logger = logging.getLogger(__name__)


@celery_app.task(bind=True, max_retries=3)
def process_receipt_ocr(self, scan_id: int, image_base64: str):
    """영수증 OCR 처리 태스크.

    1. Clova OCR API 호출
    2. 상품명 파싱
    3. 식재료 마스터 매칭
    4. 결과를 DB에 저장 (scan_history 업데이트)
    """
    logger.info(f"Processing receipt OCR: scan_id={scan_id}")

    try:
        # TODO: Implement actual OCR processing
        # 1. Call Clova OCR
        # 2. Parse receipt items
        # 3. Match with ingredient master
        # 4. Update scan_history in DB
        pass
    except Exception as exc:
        logger.error(f"Receipt OCR failed: scan_id={scan_id}, error={exc}")
        raise self.retry(exc=exc, countdown=10)
