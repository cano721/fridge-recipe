from fastapi import Depends, FastAPI, HTTPException, Header
from fastapi.middleware.cors import CORSMiddleware

from app.api import ocr, recommend, vision
from app.core.config import settings

app = FastAPI(
    title=settings.app_name,
    version="0.1.0",
    docs_url="/docs" if settings.debug else None,
)

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)


async def verify_internal_api_key(x_internal_api_key: str = Header(...)):
    if x_internal_api_key != settings.internal_api_key:
        raise HTTPException(status_code=403, detail="Invalid internal API key")


app.include_router(
    ocr.router,
    prefix="/ai/ocr",
    tags=["OCR"],
    dependencies=[Depends(verify_internal_api_key)],
)
app.include_router(
    vision.router,
    prefix="/ai/vision",
    tags=["Vision"],
    dependencies=[Depends(verify_internal_api_key)],
)
app.include_router(
    recommend.router,
    prefix="/ai/recommend",
    tags=["Recommendation"],
    dependencies=[Depends(verify_internal_api_key)],
)


@app.get("/health")
async def health_check():
    return {"status": "ok", "service": "ai-service"}
