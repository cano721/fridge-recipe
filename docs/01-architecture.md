# 01. 시스템 아키텍처 설계

## 1. 시스템 개요

### 1.1 고수준 아키텍처

```
┌─────────────────────────────────────────────────────────────────┐
│                        Client Layer                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────────────┐  │
│  │  Android App │  │   iOS App    │  │   Web (Post-MVP)     │  │
│  │  (Compose)   │  │   (CMP)      │  │   (Next.js)          │  │
│  └──────┬───────┘  └──────┬───────┘  └──────────┬───────────┘  │
│         │                 │                      │              │
│  ┌──────┴─────────────────┴──────────────────────┘              │
│  │          KMP Shared Module (비즈니스 로직)                    │
│  │  - Repository, UseCase, DTO, Validation                      │
│  │  - Ktor Client (HTTP), SQLDelight (로컬 DB)                  │
│  │  - kotlinx.serialization, Koin (DI)                          │
│  └──────────────────────────┬───────────────────────────────────┘
│                             │ HTTPS (REST API)
├─────────────────────────────┼───────────────────────────────────┤
│                        API Gateway / ALB                        │
├─────────────────────────────┼───────────────────────────────────┤
│                      Backend Layer                              │
│                             │                                   │
│  ┌──────────────────────────┴────────────────────────────────┐  │
│  │              Kotlin + Ktor (메인 백엔드)                    │  │
│  │  - 인증/인가 (JWT + OAuth2)                                │  │
│  │  - 식재료 CRUD, 레시피 CRUD                                │  │
│  │  - 사용자 프로필, 설정                                      │  │
│  │  - 알림 관리, 장보기 목록                                   │  │
│  │  - Exposed ORM + PostgreSQL                                │  │
│  └───────────────────┬──────────────────┬────────────────────┘  │
│                      │                  │                       │
│              ┌───────▼───────┐  ┌───────▼───────┐              │
│              │  PostgreSQL   │  │    Redis       │              │
│              │  (RDS)        │  │  (ElastiCache) │              │
│              └───────────────┘  └───────────────┘              │
│                      │                                         │
│  ┌───────────────────▼──────────────────────────────────────┐  │
│  │          Python + FastAPI (AI 마이크로서비스)               │  │
│  │  - 영수증 OCR (Naver Clova OCR)                           │  │
│  │  - 식재료 사진 인식 (GPT-4o Vision)                        │  │
│  │  - 레시피 추천 엔진 (콘텐츠 기반 필터링)                    │  │
│  │  - Celery + Redis (비동기 태스크)                           │  │
│  └──────────────────────────────────────────────────────────┘  │
│                                                                │
│  ┌──────────────────────────────────────────────────────────┐  │
│  │                  Infrastructure                           │  │
│  │  AWS S3 (이미지) | CloudFront (CDN) | FCM/APNs (푸시)    │  │
│  └──────────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────────┘
```

### 1.2 Kotlin 통일 스택의 이점

**백엔드를 Kotlin(Ktor)으로 선택한 이유:**

| 이점 | 설명 |
|------|------|
| **언어 통일** | 프론트(KMP) + 백엔드(Ktor) 모두 Kotlin → 컨텍스트 스위칭 제로 |
| **코드 공유** | DTO, Validation, 비즈니스 규칙을 KMP shared 모듈로 공유 가능 |
| **타입 안전** | End-to-end 강타입 → 런타임 에러 대폭 감소 |
| **코루틴** | 프론트/백 모두 Kotlin Coroutines 기반 비동기 처리 |
| **인력 효율** | 한 가지 언어만 알면 풀스택 개발 가능 |

**Python(FastAPI)을 AI 서비스로 분리한 이유:**

| 이유 | 설명 |
|------|------|
| **AI/ML 생태계** | OCR, Vision API, 추천 엔진 라이브러리가 Python에 집중 |
| **독립 스케일링** | AI 추론은 부하 패턴이 다름 → 독립적 Auto-scaling |
| **장애 격리** | AI 서비스 장애가 메인 CRUD에 영향을 주지 않음 |
| **Celery** | 비동기 태스크 처리(OCR, 배치 추천)에 Python Celery가 성숙 |

---

## 2. 프론트엔드 아키텍처 (KMP)

### 2.1 모듈 구조

```
fridge-recipe/
├── shared/                          # KMP 공유 모듈
│   ├── src/commonMain/
│   │   ├── domain/                  # 도메인 레이어
│   │   │   ├── model/              # Entity, Value Object
│   │   │   ├── repository/         # Repository 인터페이스
│   │   │   └── usecase/            # UseCase (비즈니스 로직)
│   │   ├── data/                    # 데이터 레이어
│   │   │   ├── remote/             # Ktor API 클라이언트
│   │   │   │   ├── dto/            # API 요청/응답 DTO
│   │   │   │   └── api/            # API 인터페이스 정의
│   │   │   ├── local/              # SQLDelight 로컬 DB
│   │   │   └── repository/         # Repository 구현체
│   │   ├── presentation/            # 공유 ViewModel (선택)
│   │   │   └── viewmodel/
│   │   └── core/                    # 유틸리티
│   │       ├── di/                  # Koin 모듈
│   │       └── util/
│   ├── src/androidMain/             # Android 플랫폼 구현
│   └── src/iosMain/                 # iOS 플랫폼 구현
│
├── androidApp/                      # Android 앱
│   └── src/main/
│       ├── ui/                      # Compose UI (Android 전용)
│       │   ├── screen/
│       │   ├── component/
│       │   └── theme/
│       ├── navigation/
│       └── camera/                  # CameraX 통합
│
├── iosApp/                          # iOS 앱
│   └── Sources/
│       ├── UI/                      # Compose Multiplatform UI
│       ├── Camera/                  # AVFoundation 통합
│       └── App/
│
└── backend/                         # 백엔드 (별도 리포 가능)
    ├── main-api/                    # Kotlin + Ktor
    └── ai-service/                  # Python + FastAPI
```

### 2.2 상태 관리

- **패턴**: MVI (Model-View-Intent)
- **ViewModel**: KMP shared 모듈에서 `commonMain`으로 공유
- **상태 흐름**: `StateFlow` + `SharedFlow` (Kotlin Coroutines)
- **불변 상태**: `data class` 기반 UiState

### 2.3 오프라인 전략

```
┌─────────────────────────────────────────┐
│             Repository 패턴              │
│                                         │
│  요청 → 로컬 DB 조회 → UI 즉시 반영     │
│              ↓                           │
│       네트워크 요청 (백그라운드)          │
│              ↓                           │
│     성공 → 로컬 DB 업데이트 → UI 갱신   │
│     실패 → 큐에 저장 → 재시도            │
└─────────────────────────────────────────┘
```

- **로컬 DB**: SQLDelight (멀티플랫폼 SQLite)
- **동기화**: 앱 시작 시 + 주기적 백그라운드 동기화
- **충돌 해결**: Last-Write-Wins (타임스탬프 기반, `updated_at` 비교)

**동기화 충돌 시나리오:**

| 시나리오 | 해결 방법 |
|---------|----------|
| 오프라인에서 당근 추가 + 온라인에서 당근 추가 | 서버 `updated_at`이 더 최신이면 서버 우선, 아니면 로컬 우선. 중복 방지 인덱스에 의해 합산 처리 |
| 오프라인에서 재료 삭제 + 온라인에서 같은 재료 수정 | 삭제가 우선 (tombstone 마커, 동기화 후 삭제 확정) |
| 오프라인에서 수량 300g + 온라인에서 수량 500g | LWW — `updated_at`이 더 최신인 값 채택 |
| 양쪽에서 동시에 새 재료 추가 (서로 다른 재료) | 충돌 없음 — 양쪽 모두 반영 |

- **동기화 주기**: 앱 포그라운드 진입 시 즉시 + 15분 간격 백그라운드
- **오프라인 큐**: 로컬 변경 사항을 SQLDelight `sync_queue` 테이블에 저장, 네트워크 복구 시 순차 전송

### 2.4 카메라 통합

| 플랫폼 | 라이브러리 | 용도 |
|--------|-----------|------|
| Android | CameraX | 촬영, 프리뷰, 이미지 분석 |
| iOS | AVFoundation | 촬영, 프리뷰 |
| 공유 | expect/actual | 카메라 인터페이스 추상화 |

---

## 3. 백엔드 아키텍처

### 3.1 메인 API (Kotlin + Ktor)

### 3.1.1 공통 응답 형식

#### 성공 응답
```json
{
  "success": true,
  "data": { "..." : "..." },
  "meta": { "page": 1, "size": 20, "total": 100 }
}
```
> `meta` 필드는 페이지네이션이 있는 목록 API에서만 포함됩니다.

#### 에러 응답
```json
{
  "success": false,
  "error": {
    "code": "INGREDIENT_NOT_FOUND",
    "message": "해당 식재료를 찾을 수 없습니다.",
    "details": { "..." : "..." }
  }
}
```
> `details` 필드는 선택적입니다. 유효성 검증 실패 시 필드별 오류 상세를 담습니다.

#### HTTP 상태 코드

| 코드 | 의미 |
|------|------|
| 200 | 성공 |
| 201 | 생성 성공 |
| 400 | 잘못된 요청 |
| 401 | 인증 실패 |
| 403 | 권한 없음 |
| 404 | 리소스 없음 |
| 429 | 요청 제한 초과 |
| 500 | 서버 내부 오류 |

#### 에러 코드 체계

도메인별 에러 코드 enum을 `shared/commonMain`에 정의하여 클라이언트-서버 간 공유합니다.

| 도메인 | 코드 | HTTP | 설명 |
|--------|------|------|------|
| AUTH | `AUTH_INVALID_TOKEN` | 401 | 유효하지 않은 토큰 |
| AUTH | `AUTH_TOKEN_EXPIRED` | 401 | 만료된 토큰 |
| AUTH | `AUTH_OAUTH_FAILED` | 401 | OAuth 인증 실패 |
| AUTH | `AUTH_REFRESH_REVOKED` | 401 | 폐기된 Refresh Token |
| INGREDIENT | `INGREDIENT_NOT_FOUND` | 404 | 식재료를 찾을 수 없음 |
| INGREDIENT | `INGREDIENT_DUPLICATE` | 409 | 중복 식재료 (합산 필요) |
| INGREDIENT | `INGREDIENT_MASTER_NOT_FOUND` | 404 | 마스터 DB에 없는 재료 |
| INGREDIENT | `INGREDIENT_LIMIT_EXCEEDED` | 400 | 사용자당 최대 200개 초과 |
| RECIPE | `RECIPE_NOT_FOUND` | 404 | 레시피를 찾을 수 없음 |
| RECIPE | `RECIPE_ALREADY_BOOKMARKED` | 409 | 이미 북마크됨 |
| SCAN | `SCAN_NOT_FOUND` | 404 | 스캔 결과 없음 |
| SCAN | `SCAN_PROCESSING` | 202 | 처리 중 (폴링 필요) |
| SCAN | `SCAN_FAILED` | 500 | 스캔 처리 실패 |
| SCAN | `SCAN_DAILY_LIMIT` | 429 | 일일 스캔 횟수 초과 |
| USER | `USER_NOT_FOUND` | 404 | 사용자를 찾을 수 없음 |
| COMMON | `VALIDATION_FAILED` | 400 | 입력 검증 실패 (details에 필드별 오류) |
| COMMON | `RATE_LIMIT_EXCEEDED` | 429 | 요청 제한 초과 |
| COMMON | `INTERNAL_ERROR` | 500 | 서버 내부 오류 |

#### 프로젝트 구조

```
main-api/
├── src/main/kotlin/com/fridgerecipe/
│   ├── Application.kt               # Ktor 앱 진입점
│   ├── plugins/                      # Ktor 플러그인 설정
│   │   ├── Routing.kt
│   │   ├── Serialization.kt
│   │   ├── Authentication.kt
│   │   ├── StatusPages.kt           # 에러 핸들링
│   │   └── CORS.kt
│   ├── routes/                       # API 라우트
│   │   ├── AuthRoutes.kt
│   │   ├── IngredientRoutes.kt
│   │   ├── RecipeRoutes.kt
│   │   ├── UserRoutes.kt
│   │   └── ScanRoutes.kt            # AI 서비스 프록시
│   ├── domain/                       # 도메인 모델
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   ├── data/                         # 데이터 레이어
│   │   ├── database/                 # Exposed ORM 테이블
│   │   ├── repository/
│   │   └── dto/                      # shared 모듈과 공유 가능
│   ├── auth/                         # 인증/인가
│   │   ├── JwtConfig.kt
│   │   ├── OAuthProvider.kt
│   │   └── AuthService.kt
│   └── core/                         # 공통 유틸리티
│       ├── config/
│       ├── di/                       # Koin DI
│       └── extension/
├── src/test/
└── build.gradle.kts
```

#### 핵심 의존성

```kotlin
// build.gradle.kts
dependencies {
    // Ktor Server
    implementation("io.ktor:ktor-server-core:3.1.1")
    implementation("io.ktor:ktor-server-netty:3.1.1")
    implementation("io.ktor:ktor-server-content-negotiation:3.1.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:3.1.1")
    implementation("io.ktor:ktor-server-auth:3.1.1")
    implementation("io.ktor:ktor-server-auth-jwt:3.1.1")
    implementation("io.ktor:ktor-server-cors:3.1.1")
    implementation("io.ktor:ktor-server-status-pages:3.1.1")
    implementation("io.ktor:ktor-server-call-logging:3.1.1")

    // Ktor Client (AI 서비스 호출용)
    implementation("io.ktor:ktor-client-cio:3.1.1")

    // DB
    implementation("org.jetbrains.exposed:exposed-core:0.57.0")
    implementation("org.jetbrains.exposed:exposed-dao:0.57.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.57.0")
    implementation("org.jetbrains.exposed:exposed-kotlin-datetime:0.57.0")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation("com.zaxxer:HikariCP:6.2.1")

    // Redis
    implementation("io.lettuce:lettuce-core:6.5.0")

    // DI
    implementation("io.insert-koin:koin-ktor:4.0.4")

    // 직렬화
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.0")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")
}
```

#### API 엔드포인트 설계

```
/api/v1/
├── auth/
│   ├── POST   /login              # OAuth 소셜 로그인 (카카오, Google, Apple)
│   ├── POST   /refresh            # 토큰 갱신
│   └── POST   /logout             # 로그아웃
│
├── users/
│   ├── GET    /me                  # 내 프로필
│   ├── PUT    /me                  # 프로필 수정
│   ├── PUT    /me/preferences      # 식이 선호도 설정
│   └── DELETE /me                  # 회원 탈퇴
│
├── ingredients/
│   ├── GET    /                    # 내 냉장고 식재료 목록
│   ├── POST   /                    # 식재료 등록 (단건)
│   ├── POST   /batch               # 식재료 일괄 등록 (conflictStrategy: MERGE|SEPARATE|SKIP)
│   ├── PUT    /{id}                # 식재료 수정
│   ├── DELETE /{id}                # 식재료 삭제
│   ├── POST   /delete-batch         # 일괄 삭제 (body: {"ids": [1,2,3]})
│   ├── GET    /search?q=           # 식재료 검색 (자동완성)
│   ├── GET    /categories          # 카테고리 목록
│   └── GET    /expiring            # 소비기한 임박 목록
│
├── scan/
│   ├── POST   /receipt             # 영수증 OCR 스캔 요청
│   ├── GET    /receipt/{scanId}    # 스캔 결과 조회
│   ├── POST   /photo               # 사진 식재료 인식
│   └── GET    /photo/{scanId}      # 인식 결과 조회
│
├── recipes/
│   ├── GET    /recommend           # 보유 재료 기반 추천
│   ├── GET    /{id}                # 레시피 상세
│   ├── GET    /search?q=           # 레시피 검색
│   ├── POST   /{id}/bookmark       # 즐겨찾기 추가
│   ├── DELETE /{id}/bookmark       # 즐겨찾기 제거
│   └── GET    /bookmarks           # 즐겨찾기 목록
│
├── notifications/
│   ├── GET    /settings            # 알림 설정 조회
│   ├── PUT    /settings            # 알림 설정 수정
│   └── POST   /device-token        # 푸시 토큰 등록
│
└── shopping-list/                   # (Post-MVP)
    ├── GET    /                    # 장보기 목록
    ├── POST   /from-recipe/{id}    # 레시피 부족 재료 추가
    └── PUT    /{id}/check          # 구매 완료 체크
```

### 3.2 AI 마이크로서비스 (Python + FastAPI)

```
ai-service/
├── app/
│   ├── main.py                     # FastAPI 앱
│   ├── api/
│   │   ├── ocr.py                  # POST /ai/ocr/receipt
│   │   ├── vision.py               # POST /ai/vision/ingredients
│   │   └── recommend.py            # POST /ai/recommend
│   ├── ml/
│   │   ├── ocr/
│   │   │   ├── clova_ocr.py        # Naver Clova OCR 클라이언트
│   │   │   └── receipt_parser.py   # 영수증 파싱 로직
│   │   ├── vision/
│   │   │   └── openai_vision.py    # GPT-4o Vision 클라이언트
│   │   └── recommendation/
│   │       └── engine.py           # 콘텐츠 기반 추천 엔진
│   ├── tasks/                       # Celery 비동기 태스크
│   │   ├── ocr_tasks.py
│   │   └── recommendation_tasks.py
│   └── core/
│       ├── config.py
│       └── celery_app.py
├── pyproject.toml
└── Dockerfile
```

#### Ktor ↔ FastAPI 통신

```
Ktor (메인 API)                    FastAPI (AI 서비스)
     │                                   │
     │  POST /ai/ocr/receipt             │
     │  {image_base64, user_id}          │
     │──────────────────────────────────►│
     │                                   │  → Celery 태스크 발행
     │◄── {task_id, status: processing} ─│
     │                                   │
     │  GET /ai/ocr/receipt/{task_id}    │  (폴링)
     │──────────────────────────────────►│
     │◄── {status: done, items: [...]}  ─│
     │                                   │
```

내부 통신은 **HTTP(Ktor Client)** 기반이며, VPC 내부 네트워크로 보안 처리됩니다.

#### 내부 서비스 인증

Ktor → FastAPI 호출 시 `X-Internal-Api-Key` 헤더를 사용합니다.
- API 키는 **AWS Secrets Manager**에서 주입 (하드코딩 금지)
- FastAPI 미들웨어에서 키 검증, 불일치 시 403 반환
- VPC 내부 통신이므로 TLS는 선택사항 (ALB 뒤)

**API 키 로테이션 전략:**
- **로테이션 주기**: 90일마다 키 교체
- **듀얼 키 운영**: 로테이션 시 신규 키 발급 후 구 키와 24시간 동시 유효 (무중단 교체)
  - `X-Internal-Api-Key-Primary` (신규): 즉시 활성화
  - `X-Internal-Api-Key-Secondary` (구): 24시간 유효 후 폐기
- **관리 방법**: AWS Secrets Manager 버전 관리 + Lambda 기반 자동 로테이션
- **감사 로그**: 키 사용 이력을 CloudWatch Logs에 기록

### 3.3 데이터베이스 스키마

```sql
-- 사용자
CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(255),             -- NULL 허용 (Apple Hide My Email 대응, 계정 연동은 oauth_provider+oauth_id 기준)
    nickname        VARCHAR(50) NOT NULL,
    profile_image   VARCHAR(500),
    oauth_provider  VARCHAR(20) NOT NULL,  -- kakao, google, apple
    oauth_id        VARCHAR(255) NOT NULL,
    dietary_prefs   JSONB DEFAULT '{}',    -- {vegetarian: false, allergies: ["땅콩"]}
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(oauth_provider, oauth_id)
);

-- 식재료 마스터 (관리자 관리)
CREATE TABLE ingredient_master (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL UNIQUE,
    category        VARCHAR(50) NOT NULL,   -- 채소, 육류, 해산물 등
    icon_url        VARCHAR(500),
    default_unit    VARCHAR(20),            -- 개, g, ml 등
    default_expiry_days INT,                -- 기본 소비기한 (일)
    aliases         TEXT[] DEFAULT '{}',    -- {"계란", "달걀", "egg"}
    created_at      TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_ingredient_master_aliases ON ingredient_master USING GIN(aliases);
CREATE INDEX idx_ingredient_master_trgm ON ingredient_master USING GIN(name gin_trgm_ops);

-- 사용자 냉장고 식재료
-- 간편 등록: 재료명만으로 등록 가능 (quantity, unit, expiry_date 모두 NULL 허용)
-- 중복 처리: ingredient_id + storage_type + expiry_date 조합이 같으면 수량 합산
CREATE TABLE user_ingredients (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    ingredient_id   BIGINT NOT NULL REFERENCES ingredient_master(id),
    quantity        DECIMAL(10,2),                -- NULL = 간편 등록 (유무만 관리)
    unit            VARCHAR(20),                  -- NULL 허용
    expiry_date     DATE,                         -- NULL = 소비기한 없음/미입력
    storage_type    VARCHAR(10) DEFAULT 'fridge',  -- fridge, freezer, room
    memo            VARCHAR(200),
    registered_via  VARCHAR(20) DEFAULT 'manual',  -- manual, receipt, photo
    expired_notified BOOLEAN DEFAULT FALSE,      -- 만료 알림 발송 여부 (배치 삭제 "미응답" 판단용)
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_user_ingredients_user ON user_ingredients(user_id);
CREATE INDEX idx_user_ingredients_expiry ON user_ingredients(user_id, expiry_date);
-- 간편 등록 중복 방지: 같은 재료+보관방식+소비기한 NULL 조합은 1건만 허용
CREATE UNIQUE INDEX idx_user_ingredients_dedup
    ON user_ingredients(user_id, ingredient_id, storage_type, COALESCE(expiry_date, DATE '9999-12-31'));

-- 레시피
CREATE TABLE recipes (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    cuisine_type    VARCHAR(50),            -- 한식, 양식, 중식, 일식
    difficulty      VARCHAR(10),            -- easy, medium, hard
    cooking_time    INT,                    -- 분 단위
    servings        INT DEFAULT 2,
    calories        INT,
    thumbnail_url   VARCHAR(500),
    steps           JSONB NOT NULL,         -- [{step: 1, text: "...", image_url: "..."}]
    nutrition       JSONB,                  -- {carbs: 50, protein: 30, fat: 10}
    tags            TEXT[] DEFAULT '{}',
    view_count      INT DEFAULT 0,
    avg_rating      DECIMAL(2,1) DEFAULT 0,
    source_url      VARCHAR(500),                          -- 크롤링 원본 URL
    source_type     VARCHAR(20) NOT NULL DEFAULT 'manual', -- crawled, manual, api
    created_at      TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX idx_recipes_cuisine ON recipes(cuisine_type);
CREATE INDEX idx_recipes_tags ON recipes USING GIN(tags);

-- 레시피 필요 재료
CREATE TABLE recipe_ingredients (
    id              BIGSERIAL PRIMARY KEY,
    recipe_id       BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    ingredient_id   BIGINT NOT NULL REFERENCES ingredient_master(id),
    quantity        VARCHAR(50),            -- "2큰술", "200g" 등 (자유 텍스트)
    is_essential    BOOLEAN DEFAULT TRUE,   -- 필수 vs 선택 재료
    substitute_ids  BIGINT[] DEFAULT '{}'   -- 대체 가능 재료 ID
);
CREATE INDEX idx_recipe_ingredients_recipe ON recipe_ingredients(recipe_id);
CREATE INDEX idx_recipe_ingredients_ingredient ON recipe_ingredients(ingredient_id);

-- 즐겨찾기 (API 경로: /bookmark, UI 표기: "즐겨찾기", 아이콘: ♡)
CREATE TABLE bookmarks (
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipe_id       BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    PRIMARY KEY (user_id, recipe_id)
);

-- 스캔 이력
CREATE TABLE scan_history (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    scan_type       VARCHAR(20) NOT NULL,   -- receipt, photo
    image_url       VARCHAR(500),
    status          VARCHAR(20) DEFAULT 'processing',  -- processing, done, failed
    result          JSONB,
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

-- 알림 설정
-- (push_token, device_type은 device_tokens 테이블로 분리됨)
CREATE TABLE notification_settings (
    user_id           BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    expiry_enabled    BOOLEAN DEFAULT TRUE,
    -- 알림 발송 시점은 사용자가 커스텀 가능하며, UI 배지 색상(안전/임박/긴급)과는 독립적으로 동작함
    expiry_days       INT[] DEFAULT '{3, 1, 0}',  -- D-3, D-1, 당일 알림
    -- theme_preference는 알림과 무관한 UI 설정이나, MVP에서는 별도 테이블 없이 여기서 관리
    theme_preference  VARCHAR(10) DEFAULT 'system', -- light, dark, system
    updated_at        TIMESTAMPTZ DEFAULT NOW()
);

-- 레시피 리뷰/평점
CREATE TABLE recipe_reviews (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipe_id       BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    rating          SMALLINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment         TEXT,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(user_id, recipe_id)
);
CREATE INDEX idx_recipe_reviews_recipe ON recipe_reviews(recipe_id);

-- 조리 이력 (추천 엔진 사용자 선호도 데이터)
-- recipe_id: 레시피 삭제 시 이력은 보존하되 recipe_id만 NULL로 설정 (ON DELETE SET NULL)
CREATE TABLE user_cooking_history (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipe_id       BIGINT REFERENCES recipes(id) ON DELETE SET NULL,  -- NULL 허용
    cooked_at       TIMESTAMPTZ DEFAULT NOW(),
    used_ingredients BIGINT[] DEFAULT '{}'
);
CREATE INDEX idx_cooking_history_user ON user_cooking_history(user_id);

-- 디바이스 토큰 (푸시 알림용, notification_settings에서 분리)
-- 토큰 갱신 전략: 같은 기기에서 토큰 변경 시 기존 row를 is_active=false로 비활성화 후 새 row INSERT
-- 앱 실행 시 POST /notifications/device-token → UPSERT (device_type 기준 기존 토큰 비활성화)
CREATE TABLE device_tokens (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token           VARCHAR(500) NOT NULL,
    device_type     VARCHAR(10) NOT NULL,  -- android, ios
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(token)
);
CREATE INDEX idx_device_tokens_user ON device_tokens(user_id);

-- Refresh Token (JWT 토큰 갱신용)
-- Refresh Token Rotation: 사용 시마다 새 토큰 발급, 이전 토큰 무효화
-- 사용자당 최대 5개 (다중 기기), FIFO로 오래된 것부터 폐기
CREATE TABLE refresh_tokens (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash      VARCHAR(64) NOT NULL UNIQUE,   -- SHA-256 해시 저장 (원문 저장 금지)
    device_info     VARCHAR(200),                   -- User-Agent 기반 기기 식별
    expires_at      TIMESTAMPTZ NOT NULL,           -- 발급 후 14일
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    revoked_at      TIMESTAMPTZ                     -- NULL이면 유효, 값 있으면 폐기됨
);
CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_expires ON refresh_tokens(expires_at) WHERE revoked_at IS NULL;

-- 추가 인덱스
CREATE INDEX idx_recipes_title_trgm ON recipes USING GIN(title gin_trgm_ops);
CREATE INDEX idx_scan_history_user ON scan_history(user_id);
```

---

## 4. AI/ML 파이프라인

### 4.1 영수증 OCR

**선택: Naver Clova OCR (Document OCR - Receipt 모델)**

| 이유 | 설명 |
|------|------|
| 한국 영수증 특화 | 상품명, 수량, 단가를 구조화된 JSON으로 반환 |
| 최고 한글 인식률 | ICDAR 2019 4개 부문 1위 |
| 마트 포맷 최적화 | GS25, CU, 이마트 등 한국 유통 영수증 |
| 비용 | 월 무료 한도 제공, MVP 비용 절감 |

**처리 흐름**: 이미지 업로드 → S3 저장 → S3 Pre-signed URL 발급 → Celery 태스크에 URL 전달 → Clova OCR API → 상품명 파싱 → 식재료 매칭 → 결과 반환

> **메모리 최적화**: 이미지를 base64로 인메모리 변환하면 10MB 이미지가 ~13.3MB가 되어 동시 요청 시 메모리 부족 위험이 있습니다. S3 Pre-signed URL을 AI 서비스에 전달하여 직접 다운로드하게 하면 Ktor 서버의 메모리 부담이 없어집니다. Celery Worker의 `--concurrency=2`로 제한하여 AI 서비스(1 vCPU, 2GB)의 메모리를 안전하게 운영합니다.

### 4.2 식재료 사진 인식

**선택: GPT-4o Vision API**

| 이유 | 설명 |
|------|------|
| 다품목 인식 | 한 장의 사진에서 모든 식재료를 한 번에 인식 |
| 한국어 이해 | 프롬프트로 "한국어 식재료명" 직접 반환 지시 가능 |
| 최소 개발 공수 | API 호출만으로 동작, 후처리 불필요 |
| 맥락 이해 | "깻잎 vs 들깻잎" 같은 미묘한 구분 가능 |

### 4.3 레시피 추천 엔진

**선택: 콘텐츠 기반 필터링 (스코어링 매칭)**

```
총점 = 0.40 × 재료매칭률
     + 0.25 × 소비기한임박활용도
     + 0.15 × 사용자선호도
     + 0.10 × 인기도
     - 0.10 × 부족재료패널티
```

- **Cold-start 문제 없음**: 사용자 데이터 없이도 즉시 추천 가능
- **설명 가능**: "보유 재료 7/9개 매칭 (78%)" 으로 추천 이유 설명
- **소비기한 활용**: 임박 재료를 사용하는 레시피에 가산점
- **간편 등록 호환**: 수량/단위 없이 재료 유무(presence)만으로 매칭 동작

**매칭 표시 기준:**
- 필수 재료(`is_essential=true`) 1개 이상 보유 AND 매칭률 30% 이상만 표시
- 대체 재료(`substitute_ids`) 보유 시 해당 재료 보유로 간주
- 만료된 재료(`expiry_date < today`)는 매칭 계산에서 제외

| 구간 | 매칭률 | 라벨 |
|------|--------|------|
| A | 80%+ | "바로 요리 가능" |
| B | 50~79% | "재료 조금 부족" |
| C | 30~49% | "도전해보세요" |
| - | 30% 미만 | 표시 안 함 |

**스코어 항목별 계산 방법:**

| 항목 (가중치) | 계산 방법 |
|-------------|----------|
| 재료매칭률 (0.40) | `보유 필수재료 수 / 전체 필수재료 수` (대체 재료 보유 시 해당 재료 보유로 간주) |
| 소비기한임박활용도 (0.25) | `Σ(보유 재료별 상태 점수) / 레시피 필수재료 수`. 상태 점수: 긴급(D-1~당일)=1.0, 임박(D-2~D-3)=0.7, 안전(D-4+)=0.0, 미보유=0.0. 보유 재료 중 임박/긴급이 0건이면 이 항목 전체 0.0 |
| 사용자선호도 (0.15) | `f(조리이력 cuisine_type 빈도, dietary_prefs 일치도)`. 이력 0건(Cold Start)일 때 기본값 0.5 |
| 인기도 (0.10) | `(view_count 정규화 + avg_rating / 5.0) / 2` |
| 부족재료패널티 (0.10) | `부족 필수재료 수 / 전체 필수재료 수` |

> **사용자선호도 상세**: `조리이력점수(0.6) + 식이선호도점수(0.4)`. 조리이력점수 = 최근 30일 조리한 레시피의 `cuisine_type` 빈도를 정규화한 값. 식이선호도점수 = 레시피가 사용자의 `dietary_prefs`(알레르기, 채식 등)를 위반하면 0.0, 완전 부합하면 1.0.

---

## 5. 인프라스트럭처

### 5.1 클라우드: AWS (서울 리전 ap-northeast-2)

```
                    ┌─────────────────────┐
                    │    Route 53 (DNS)    │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │   CloudFront (CDN)   │
                    └──────────┬──────────┘
                               │
                    ┌──────────▼──────────┐
                    │   ALB (Load Balancer)│
                    └───┬─────────────┬───┘
                        │             │
           ┌────────────▼──┐  ┌───────▼──────────┐
           │  ECS Fargate  │  │   ECS Fargate    │
           │  (Ktor API    │  │   (FastAPI AI    │
           │   x 2~4)      │  │    + Celery)     │
           └───────┬───────┘  └───────┬──────────┘
                   │                  │
       ┌───────────┼──────────────────┘
       │           │
       ▼           ▼
  ┌─────────┐ ┌─────────┐ ┌─────────┐
  │   RDS   │ │  Redis  │ │   S3    │
  │ Postgres│ │ElastiCa.│ │ (이미지)│
  └─────────┘ └─────────┘ └─────────┘
```

### 5.2 MVP 월간 비용 추정

| 서비스 | 사양 | 비용/월 |
|--------|------|---------|
| ECS Fargate (Ktor) | 0.5 vCPU, 1GB x 2 | ~$30 |
| ECS Fargate (FastAPI+Celery) | 1 vCPU, 2GB x 1 | ~$30 |
| RDS PostgreSQL | db.t4g.micro, 20GB | ~$15 |
| ElastiCache Redis | cache.t4g.micro | ~$12 |
| S3 + CloudFront | 10GB, 50GB 전송 | ~$5 |
| Clova OCR | 월 500건 무료 내 | $0 |
| OpenAI API | 월 1,000건 Vision | ~$20 |
| ALB | Application Load Balancer | ~$20 |
| NAT Gateway | 1 AZ, 10GB 처리 | ~$40 |
| **합계** | | **~$172/월** |

> **NAT Gateway 비용 최적화**: NAT Gateway가 전체 비용의 23%를 차지합니다. VPC Endpoint(S3, ECR용)를 추가하여 AWS 서비스 통신은 NAT를 우회하고, 외부 API(Clova, OpenAI) 호출만 NAT를 통과하도록 설정하면 데이터 처리량을 줄여 비용을 절감할 수 있습니다. MVP 초기에는 Public Subnet + Security Group 조합으로 NAT를 생략하는 것도 가능합니다.

### 5.3 보안

| 영역 | 방식 |
|------|------|
| 인증 | JWT (Access 30분 + Refresh 14일) |
| OAuth | 카카오, Google, Apple (MVP) |
| API 보안 | Rate Limiting (아래 상세 참조) |
| HTTPS | ALB SSL 종료, 내부 VPC 통신 |
| 시크릿 | AWS Secrets Manager |
| 입력 검증 | kotlinx.serialization 스키마, Pydantic v2 |
| 파일 업로드 | 이미지만, 10MB 제한, Content-Type 검증 |

#### Rate Limiting 상세

- **알고리즘**: Sliding Window Counter (Redis `ZADD` 기반)
- **일반 API**: 100 req/min/user
- **AI 스캔 API (OCR/Vision)**: 10 req/day/user (비용 제어)
- **초과 시**: HTTP 429 반환 + `Retry-After` 헤더 포함
- **응답 헤더**:
  - `X-RateLimit-Limit`: 허용 최대 요청 수
  - `X-RateLimit-Remaining`: 남은 요청 수
  - `X-RateLimit-Reset`: 윈도우 리셋 시각 (Unix timestamp)

#### JWT 토큰 전략

- **Access Token**: 30분, 헤더 `Authorization: Bearer {token}`
- **Refresh Token**: 14일, httpOnly 쿠키 또는 Body
- **Refresh Token Rotation**: 사용 시마다 새 Refresh Token 발급, 이전 토큰 무효화
- **토큰 블랙리스트**: Redis에 로그아웃/탈퇴 시 Access Token 저장 (TTL = 남은 만료시간)
- **다중 기기**: 사용자당 최대 5개 Refresh Token 허용, FIFO로 오래된 것부터 폐기

### 5.4 CI/CD 파이프라인

```
GitHub Actions 워크플로우:

main 브랜치:
  Push → Lint → Unit Test → Build → Docker Build → ECR Push → ECS Deploy (prod)

develop 브랜치:
  Push → Lint → Unit Test → Build → Docker Build → ECR Push → ECS Deploy (staging)

PR:
  Open → Lint → Unit Test → Build → 코드 리뷰 봇
```

| 단계 | 도구 | 비고 |
|------|------|------|
| 린트 | ktlint (Kotlin), ruff (Python) | PR 차단 |
| 테스트 | JUnit5 + Kotest (Ktor), pytest (FastAPI) | 커버리지 80% 목표 |
| 빌드 | Gradle (Ktor), Docker (FastAPI) | 멀티스테이지 빌드 |
| 배포 | AWS ECS Rolling Update | Blue/Green은 Post-MVP |

### 5.5 환경 분리

| 환경 | 용도 | 인프라 |
|------|------|--------|
| local | 개발자 로컬 | Docker Compose (PostgreSQL + Redis + FastAPI) |
| staging | QA/통합 테스트 | AWS ECS (최소 사양, 단일 인스턴스) |
| production | 서비스 운영 | AWS ECS (오토스케일링, 멀티 AZ) |

- 환경 변수: AWS Secrets Manager (prod/staging), `.env` (local)
- DB 마이그레이션: Flyway (Kotlin) 또는 Alembic (Python), 환경별 독립 실행

### 5.6 장애 복구 및 가용성

| 항목 | 전략 |
|------|------|
| DB 백업 | RDS 자동 스냅샷 (일 1회, 7일 보관) |
| DB 복구 | Point-in-Time Recovery, RTO 30분 목표 |
| 서비스 헬스체크 | ALB + ECS 헬스체크 (30초 간격) |
| 자동 복구 | ECS 태스크 자동 재시작 (unhealthy 시) |
| 모니터링 | CloudWatch 알람 → SNS → Slack/이메일 |
| 에러 추적 | Sentry (Kotlin + Python) |
| 로깅 | CloudWatch Logs, 구조화 JSON 로깅 |

> MVP 단계에서는 Single-AZ로 시작하고, DAU 1,000 돌파 시 Multi-AZ + Read Replica 전환 계획

### 5.7 배치/스케줄링

Ktor 서버 내 `kotlinx-coroutines` 기반 스케줄러로 주기적 배치 작업을 실행합니다.

| 작업 | 주기 | 실행 환경 | 설명 |
|------|------|----------|------|
| 만료 재료 자동 삭제 | 매일 03:00 KST | Ktor (코루틴) | `expiry_date + 7일 < today` AND 사용자 미응답 재료 삭제 + 인앱 알림 생성 |
| 소비기한 푸시 알림 발송 | 매일 09:00 KST | Ktor (코루틴) | `notification_settings.expiry_days` 기준으로 대상 사용자 추출 → FCM/APNs 발송 |
| 만료 Refresh Token 정리 | 매일 04:00 KST | Ktor (코루틴) | `expires_at < now()` OR `revoked_at IS NOT NULL` 토큰 삭제 |
| Redis 캐시 워밍 | 앱 배포 시 | CI/CD 후처리 | 인기 레시피 추천 결과 사전 캐시 |

```kotlin
// 배치 스케줄러 예시 (Ktor Application 모듈 내)
fun Application.configureScheduler() {
    launch {
        while (isActive) {
            delay(calculateNextRunDelay("03:00"))
            expiredIngredientCleanupService.execute()
        }
    }
}
```

> MVP에서는 Ktor 내장 코루틴 스케줄러로 충분합니다. DAU 5,000+ 시점에서 별도 배치 서비스 또는 AWS EventBridge + Lambda 조합으로 분리 검토.

### 5.8 OCR 폴링 전략

Celery 비동기 태스크의 결과를 클라이언트가 폴링하는 방식:

- **폴링 간격**: 1초 (첫 5회) → 2초 (이후)
- **최대 대기**: 30초 (타임아웃 시 실패 처리)
- **Post-MVP 개선**: WebSocket/SSE 기반 실시간 결과 전달 (Ktor WebSocket 지원)

---

## 6. KMP ↔ Ktor 코드 공유

Kotlin 통일 스택의 가장 큰 장점은 **DTO와 검증 로직 공유**입니다.

```kotlin
// shared 모듈 (commonMain) - 클라이언트와 서버 모두 사용
@Serializable
data class IngredientRequest(
    val ingredientId: Long,
    val quantity: Double? = null,
    val unit: String? = null,
    val expiryDate: LocalDate? = null,
    val storageType: StorageType = StorageType.FRIDGE,
    val memo: String? = null
) {
    fun validate(): List<String> {
        val errors = mutableListOf<String>()
        if (quantity != null && quantity <= 0) errors.add("수량은 0보다 커야 합니다")
        if (memo != null && memo.length > 200) errors.add("메모는 200자 이내여야 합니다")
        return errors
    }
}

@Serializable
data class RecipeRecommendation(
    val recipeId: Long,
    val title: String,
    val thumbnailUrl: String?,
    val matchRatio: Double,           // 0.0 ~ 1.0
    val matchedCount: Int,
    val totalRequired: Int,
    val missingIngredients: List<String>,
    val cookingTime: Int,
    val difficulty: String,
    val calories: Int?
)

@Serializable
enum class StorageType { FRIDGE, FREEZER, ROOM }
```

---

## 7. MVP 스프린트 로드맵 (20주)

> **레시피 데이터 전략**: 크롤러 개발 + 정제 + 검수 공수를 감안하여, **1차 MVP는 수동 입력 + 공공 레시피 API(농촌진흥청 공공데이터 등) 활용**으로 빠르게 1,000개를 확보한다. 크롤러 기반 수집은 저작권 검토 완료 후 Post-MVP에서 진행한다.

| 스프린트 | 기간 | 내용 |
|---------|------|------|
| **1-2** | 2주 | 프로젝트 셋업: KMP 구조, Ktor 구조, Docker Compose, CI/CD, DB 스키마 v1 |
| **3-5** | 3주 | 인증(카카오 + Google + Apple OAuth + JWT) + 식재료 수동 등록 CRUD + 자동완성 검색 |
| **6-8** | 3주 | 영수증 OCR(카메라 + Clova OCR) + 냉장고 대시보드 UI |
| **9-11** | 3주 | 레시피 데이터 수집(공공 API + 수동 입력, 1,000개) + 추천 엔진 + 추천/상세 UI |
| **12-13** | 2주 | 레시피 데이터 정제·검수 + source_type 태깅 + 추천 품질 개선 |
| **14-16** | 3주 | 소비기한 알림(FCM/APNs) + 프로필/설정 + 오프라인 캐시(SQLDelight) |
| **17-18** | 2주 | 통합 테스트, 성능 최적화, 버그 수정 |
| **19-20** | 2주 | 앱스토어 심사 제출, 프로덕션 배포, 모니터링 안정화 |

---

## 8. 기술 스택 총정리

| 계층 | 기술 | 버전 | 선정 이유 |
|------|------|------|----------|
| **모바일 (공유)** | Kotlin Multiplatform | 2.1.x | Android/iOS 로직 100% 공유 |
| **모바일 UI** | Compose Multiplatform | 1.8.x | iOS Stable, 선언적 UI 공유 |
| **모바일 네트워크** | Ktor Client | 3.1.x | KMP 공식 HTTP 클라이언트 |
| **모바일 로컬 DB** | SQLDelight | 2.0.x | KMP 타입 안전 로컬 DB |
| **모바일 DI** | Koin | 4.0.x | KMP 네이티브 DI |
| **모바일 직렬화** | kotlinx.serialization | 1.8.x | KMP 공식, 서버와 공유 |
| **메인 백엔드** | Kotlin + Ktor Server | 3.1.x | 코루틴 기반, KMP 코드 공유 |
| **백엔드 ORM** | Exposed | 0.57.x | Kotlin 네이티브 ORM |
| **AI 서비스** | Python + FastAPI | 0.115.x | AI/ML 생태계 활용 |
| **비동기 태스크** | Celery | 5.4.x | OCR/추천 비동기 처리 |
| **데이터베이스** | PostgreSQL | 16 | JSONB, pg_trgm |
| **캐시** | Redis | 7.x | 캐시 + 메시지 브로커 |
| **영수증 OCR** | Naver Clova OCR | - | 한국 영수증 특화 |
| **식재료 인식** | OpenAI GPT-4o Vision | - | 다품목 한 번에 인식 |
| **추천 엔진** | 자체 (콘텐츠 기반) | - | Cold-start 없음 |
| **인프라** | AWS (ECS, RDS, S3) | - | 서울 리전, 비용 효율 |
| **CI/CD** | GitHub Actions | - | GitHub 네이티브 |
| **모니터링** | CloudWatch + Sentry | - | 메트릭 + 에러 추적 |
| **웹 (Post-MVP)** | Next.js | 15.x | SSR/SSG, SEO |
