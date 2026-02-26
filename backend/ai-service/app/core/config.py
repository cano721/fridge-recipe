from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    internal_api_key: str = "dev-internal-key"
    redis_url: str = "redis://localhost:6379/0"
    celery_broker_url: str = "redis://localhost:6379/1"
    celery_result_backend: str = "redis://localhost:6379/1"

    # External APIs (스텁)
    clova_ocr_api_url: str = ""
    clova_ocr_secret_key: str = ""
    openai_api_key: str = ""

    model_config = {"env_file": ".env", "extra": "ignore"}


settings = Settings()
