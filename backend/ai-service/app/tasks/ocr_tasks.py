"""Celery OCR/Vision 비동기 태스크"""
import asyncio

from app.core.celery_app import celery_app
from app.ml.ocr.clova_ocr import ClovaOcrClient
from app.ml.ocr.receipt_parser import ReceiptParser
from app.ml.vision.openai_vision import VisionClient


@celery_app.task(bind=True, name="ocr.receipt")
def receipt_ocr_task(self, image_url: str, user_id: int) -> dict:
    """영수증 OCR 비동기 태스크"""
    loop = asyncio.new_event_loop()
    try:
        client = ClovaOcrClient()
        ocr_result = loop.run_until_complete(client.recognize_receipt(image_url))
        parser = ReceiptParser()
        items = parser.parse(ocr_result)
        return {"status": "done", "items": items}
    except Exception as exc:
        self.update_state(state="FAILURE", meta={"error": str(exc)})
        raise
    finally:
        loop.close()


@celery_app.task(bind=True, name="vision.ingredients")
def photo_vision_task(self, image_url: str, user_id: int) -> dict:
    """사진 식재료 인식 비동기 태스크"""
    loop = asyncio.new_event_loop()
    try:
        client = VisionClient()
        ingredients = loop.run_until_complete(client.recognize_ingredients(image_url))
        return {"status": "done", "ingredients": ingredients}
    except Exception as exc:
        self.update_state(state="FAILURE", meta={"error": str(exc)})
        raise
    finally:
        loop.close()
