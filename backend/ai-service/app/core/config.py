import os

from pydantic_settings import BaseSettings


class Settings(BaseSettings):
    app_name: str = "Fridge Recipe AI Service"
    debug: bool = True

    # Internal API Key
    internal_api_key: str = os.getenv("INTERNAL_API_KEY", "dev-internal-key")

    # Database
    database_url: str = os.getenv("DATABASE_URL", "postgresql://postgres:postgres@localhost:5432/fridgerecipe")

    # Redis
    redis_url: str = os.getenv("REDIS_URL", "redis://localhost:6379/0")

    # Naver Clova OCR
    clova_ocr_api_url: str = os.getenv("CLOVA_OCR_API_URL", "")
    clova_ocr_secret_key: str = os.getenv("CLOVA_OCR_SECRET_KEY", "")

    # OpenAI
    openai_api_key: str = os.getenv("OPENAI_API_KEY", "")
    openai_model: str = os.getenv("OPENAI_MODEL", "gpt-4o")

    # Main API callback
    main_api_url: str = os.getenv("MAIN_API_URL", "http://localhost:8080")

    model_config = {"env_prefix": "AI_"}


settings = Settings()
