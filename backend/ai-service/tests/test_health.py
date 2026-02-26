from unittest.mock import MagicMock, patch

from fastapi.testclient import TestClient

from app.main import app

client = TestClient(app)


def test_health_check():
    response = client.get("/ai/health")
    assert response.status_code == 200
    data = response.json()
    assert data["status"] == "ok"


def test_ocr_requires_api_key():
    response = client.post("/ai/ocr/receipt", json={"image_url": "http://example.com/img.jpg", "user_id": 1})
    assert response.status_code == 403


def test_ocr_with_valid_api_key():
    with patch("app.api.ocr.receipt_ocr_task") as mock_task:
        mock_task.delay.return_value = MagicMock(id="test-task-id")
        response = client.post(
            "/ai/ocr/receipt",
            json={"image_url": "http://example.com/img.jpg", "user_id": 1},
            headers={"X-Internal-Api-Key": "dev-internal-key"},
        )
    assert response.status_code == 202
