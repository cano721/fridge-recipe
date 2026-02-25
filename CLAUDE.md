# Fridge Recipe - 냉장고 레시피 추천 앱

## 프로젝트 구조

```
fridge-recipe/
├── shared/                    # KMP 공유 모듈 (도메인 모델, DTO, Repository 인터페이스)
├── androidApp/                # Android 앱 (Compose UI, Navigation)
├── iosApp/                    # iOS 앱 (Compose Multiplatform, 추후 구현)
├── backend/
│   ├── main-api/              # Kotlin + Ktor 메인 백엔드
│   └── ai-service/            # Python + FastAPI AI 마이크로서비스
├── docs/                      # 설계 문서
└── docker-compose.yml         # 로컬 개발 환경
```

## 기술 스택

- **모바일**: Kotlin Multiplatform + Compose Multiplatform
- **메인 백엔드**: Kotlin + Ktor 3.1 + Exposed ORM + PostgreSQL 16
- **AI 서비스**: Python + FastAPI + Celery + Redis
- **인프라**: Docker Compose (로컬), AWS ECS Fargate (프로덕션)

## 로컬 개발

```bash
# 백엔드 인프라 실행
docker-compose up -d postgres redis

# Ktor API 실행
./gradlew :backend:main-api:run

# FastAPI AI 서비스 실행
cd backend/ai-service && uvicorn app.main:app --reload

# Android 앱 빌드
./gradlew :androidApp:assembleDebug
```

## 코드 컨벤션

- **Kotlin**: ktlint 기본 설정
- **Python**: ruff (line-length=120)
- **커밋 메시지**: 한국어 허용, conventional commits 형식 권장
- **패키지**: `com.fridgerecipe` (Kotlin), `app` (Python)

## API 엔드포인트

- 메인 API: `http://localhost:8080/api/v1/`
- AI 서비스: `http://localhost:8000/ai/`
- Health check: `GET /health`

## 설계 문서

- `docs/01-architecture.md`: 시스템 아키텍처
- `docs/02-prd-analysis.md`: 요구사항 분석
- `docs/03-design-system.md`: 디자인 시스템 스펙
