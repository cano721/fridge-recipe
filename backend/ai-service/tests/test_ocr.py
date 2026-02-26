from unittest.mock import MagicMock, patch

from fastapi.testclient import TestClient

from app.main import app
from app.ml.ocr.receipt_parser import ReceiptParser

client = TestClient(app)
HEADERS = {"X-Internal-Api-Key": "dev-internal-key"}


# --- API 엔드포인트 테스트 ---

def test_scan_receipt_returns_202():
    with patch("app.api.ocr.receipt_ocr_task") as mock_task:
        mock_task.delay.return_value = MagicMock(id="test-task-123")
        response = client.post(
            "/ai/ocr/receipt",
            json={"image_url": "http://example.com/receipt.jpg", "user_id": 1},
            headers=HEADERS,
        )
    assert response.status_code == 202
    data = response.json()
    assert data["task_id"] == "test-task-123"
    assert data["status"] == "processing"


def test_scan_receipt_requires_api_key():
    response = client.post(
        "/ai/ocr/receipt",
        json={"image_url": "http://example.com/receipt.jpg", "user_id": 1},
    )
    assert response.status_code == 403


def test_get_scan_result_processing():
    with patch("app.api.ocr.celery_app") as mock_celery:
        mock_result = MagicMock()
        mock_result.ready.return_value = False
        mock_celery.AsyncResult.return_value = mock_result

        response = client.get("/ai/ocr/receipt/some-task-id", headers=HEADERS)

    assert response.status_code == 200
    data = response.json()
    assert data["task_id"] == "some-task-id"
    assert data["status"] == "processing"
    assert data["items"] is None


def test_get_scan_result_done():
    with patch("app.api.ocr.celery_app") as mock_celery:
        mock_result = MagicMock()
        mock_result.ready.return_value = True
        mock_result.successful.return_value = True
        mock_result.result = {"status": "done", "items": [{"name": "두부", "quantity": 1, "unit": "개", "confidence": 0.9}]}
        mock_celery.AsyncResult.return_value = mock_result

        response = client.get("/ai/ocr/receipt/done-task-id", headers=HEADERS)

    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "done"
    assert len(data["items"]) == 1
    assert data["items"][0]["name"] == "두부"


def test_get_scan_result_failed():
    with patch("app.api.ocr.celery_app") as mock_celery:
        mock_result = MagicMock()
        mock_result.ready.return_value = True
        mock_result.successful.return_value = False
        mock_result.result = Exception("OCR API error")
        mock_celery.AsyncResult.return_value = mock_result

        response = client.get("/ai/ocr/receipt/failed-task-id", headers=HEADERS)

    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "failed"
    assert data["error"] is not None


# --- ReceiptParser 단위 테스트 ---

MOCK_OCR_RESULT = {
    "images": [
        {
            "receipt": {
                "result": {
                    "storeInfo": {"name": {"text": "테스트마트"}},
                    "subResults": [
                        {
                            "items": [
                                {
                                    "name": {"text": "두부", "formatted": {"value": "두부"}},
                                    "count": {"text": "2"},
                                    "price": {"unitPrice": {"text": "1500"}},
                                },
                                {
                                    "name": {"text": "계란 10구", "formatted": {"value": "계란 10구"}},
                                    "count": {"text": "1"},
                                    "price": {"unitPrice": {"text": "3000"}},
                                },
                                {
                                    "name": {"text": "비닐봉투", "formatted": {"value": "비닐봉투"}},
                                    "count": {"text": "1"},
                                    "price": {"unitPrice": {"text": "100"}},
                                },
                                {
                                    "name": {"text": "포인트 적립", "formatted": {"value": "포인트 적립"}},
                                    "count": {"text": "1"},
                                    "price": {"unitPrice": {"text": "0"}},
                                },
                            ]
                        }
                    ],
                }
            }
        }
    ]
}


def test_receipt_parser_excludes_keywords():
    parser = ReceiptParser()
    items = parser.parse(MOCK_OCR_RESULT)
    names = [item["name"] for item in items]
    assert not any("비닐" in n or "봉투" in n for n in names)
    assert not any("포인트" in n for n in names)


def test_receipt_parser_returns_ingredients():
    parser = ReceiptParser()
    items = parser.parse(MOCK_OCR_RESULT)
    assert len(items) == 2


def test_receipt_parser_quantity():
    parser = ReceiptParser()
    items = parser.parse(MOCK_OCR_RESULT)
    tofu = next(i for i in items if "두부" in i["name"])
    assert tofu["quantity"] == 2


def test_receipt_parser_confidence():
    parser = ReceiptParser()
    items = parser.parse(MOCK_OCR_RESULT)
    for item in items:
        assert 0.0 <= item["confidence"] <= 1.0


def test_receipt_parser_empty_result():
    parser = ReceiptParser()
    items = parser.parse({})
    assert items == []


def test_receipt_parser_all_excluded():
    parser = ReceiptParser()
    ocr_result = {
        "images": [
            {
                "receipt": {
                    "result": {
                        "subResults": [
                            {
                                "items": [
                                    {"name": {"text": "비닐봉투", "formatted": {"value": "비닐봉투"}}, "count": {"text": "1"}, "price": {}},
                                    {"name": {"text": "합계", "formatted": {"value": "합계"}}, "count": {"text": "1"}, "price": {}},
                                ]
                            }
                        ]
                    }
                }
            }
        ]
    }
    items = parser.parse(ocr_result)
    assert items == []
