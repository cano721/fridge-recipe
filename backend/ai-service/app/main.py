from fastapi import FastAPI

from app.api import health, ocr, recommend, vision
from app.core.security import InternalApiKeyMiddleware

app = FastAPI(
    title="Fridge Recipe AI Service",
    version="0.1.0",
    docs_url="/ai/docs",
    openapi_url="/ai/openapi.json",
)

app.add_middleware(InternalApiKeyMiddleware)

app.include_router(health.router, prefix="/ai")
app.include_router(ocr.router, prefix="/ai")
app.include_router(vision.router, prefix="/ai")
app.include_router(recommend.router, prefix="/ai")
