from celery import Celery

from app.core.config import settings

celery_app = Celery(
    "fridge_recipe_ai",
    broker=settings.redis_url,
    backend=settings.redis_url,
)

celery_app.conf.update(
    task_serializer="json",
    accept_content=["json"],
    result_serializer="json",
    timezone="Asia/Seoul",
    enable_utc=True,
    task_track_started=True,
    task_time_limit=120,
    task_soft_time_limit=90,
)
