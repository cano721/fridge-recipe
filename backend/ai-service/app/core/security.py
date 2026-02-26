from fastapi import Request, Response
from starlette.middleware.base import BaseHTTPMiddleware
from starlette.responses import JSONResponse

from app.core.config import settings

HEALTH_PATHS = {"/ai/health", "/ai/docs", "/ai/openapi.json"}


class InternalApiKeyMiddleware(BaseHTTPMiddleware):
    async def dispatch(self, request: Request, call_next) -> Response:
        if request.url.path in HEALTH_PATHS:
            return await call_next(request)

        api_key = request.headers.get("X-Internal-Api-Key")
        if api_key != settings.internal_api_key:
            return JSONResponse(
                status_code=403,
                content={"detail": "Invalid or missing API key"},
            )

        return await call_next(request)
