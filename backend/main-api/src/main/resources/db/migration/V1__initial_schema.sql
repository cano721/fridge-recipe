-- V1: Initial schema for Fridge Recipe MVP
-- Extension for trigram search
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- 사용자
CREATE TABLE IF NOT EXISTS users (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(255) UNIQUE,
    nickname        VARCHAR(50) NOT NULL,
    profile_image   VARCHAR(500),
    oauth_provider  VARCHAR(20) NOT NULL,
    oauth_id        VARCHAR(255) NOT NULL,
    dietary_prefs   JSONB DEFAULT '{}',
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(oauth_provider, oauth_id)
);

-- 식재료 마스터 (관리자 관리)
CREATE TABLE IF NOT EXISTS ingredient_master (
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(100) NOT NULL UNIQUE,
    category            VARCHAR(50) NOT NULL,
    icon_url            VARCHAR(500),
    default_unit        VARCHAR(20),
    default_expiry_days INT,
    aliases             TEXT[] DEFAULT '{}',
    created_at          TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_ingredient_master_aliases ON ingredient_master USING GIN(aliases);
CREATE INDEX IF NOT EXISTS idx_ingredient_master_trgm ON ingredient_master USING GIN(name gin_trgm_ops);

-- 사용자 냉장고 식재료
CREATE TABLE IF NOT EXISTS user_ingredients (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    ingredient_id   BIGINT NOT NULL REFERENCES ingredient_master(id),
    quantity        DECIMAL(10,2),
    unit            VARCHAR(20),
    expiry_date     DATE,
    storage_type    VARCHAR(10) DEFAULT 'fridge',
    memo            VARCHAR(200),
    registered_via  VARCHAR(20) DEFAULT 'manual',
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_user_ingredients_user ON user_ingredients(user_id);
CREATE INDEX IF NOT EXISTS idx_user_ingredients_expiry ON user_ingredients(user_id, expiry_date);

-- 레시피
CREATE TABLE IF NOT EXISTS recipes (
    id              BIGSERIAL PRIMARY KEY,
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    cuisine_type    VARCHAR(50),
    difficulty      VARCHAR(10),
    cooking_time    INT,
    servings        INT DEFAULT 2,
    calories        INT,
    thumbnail_url   VARCHAR(500),
    steps           JSONB NOT NULL DEFAULT '[]',
    nutrition       JSONB,
    tags            TEXT[] DEFAULT '{}',
    view_count      INT DEFAULT 0,
    avg_rating      DECIMAL(2,1) DEFAULT 0,
    source_url      VARCHAR(500),
    source_type     VARCHAR(20) NOT NULL DEFAULT 'manual',
    created_at      TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_recipes_cuisine ON recipes(cuisine_type);
CREATE INDEX IF NOT EXISTS idx_recipes_tags ON recipes USING GIN(tags);
CREATE INDEX IF NOT EXISTS idx_recipes_title_trgm ON recipes USING GIN(title gin_trgm_ops);

-- 레시피 필요 재료
CREATE TABLE IF NOT EXISTS recipe_ingredients (
    id              BIGSERIAL PRIMARY KEY,
    recipe_id       BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    ingredient_id   BIGINT NOT NULL REFERENCES ingredient_master(id),
    quantity        VARCHAR(50),
    is_essential    BOOLEAN DEFAULT TRUE,
    substitute_ids  BIGINT[] DEFAULT '{}'
);
CREATE INDEX IF NOT EXISTS idx_recipe_ingredients_recipe ON recipe_ingredients(recipe_id);
CREATE INDEX IF NOT EXISTS idx_recipe_ingredients_ingredient ON recipe_ingredients(ingredient_id);

-- 북마크
CREATE TABLE IF NOT EXISTS bookmarks (
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipe_id       BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    PRIMARY KEY (user_id, recipe_id)
);

-- 스캔 이력
CREATE TABLE IF NOT EXISTS scan_history (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    scan_type       VARCHAR(20) NOT NULL,
    image_url       VARCHAR(500),
    status          VARCHAR(20) DEFAULT 'processing',
    result          JSONB,
    created_at      TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_scan_history_user ON scan_history(user_id);

-- 알림 설정
CREATE TABLE IF NOT EXISTS notification_settings (
    user_id           BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    expiry_enabled    BOOLEAN DEFAULT TRUE,
    expiry_days       INT[] DEFAULT '{3, 1}',
    theme_preference  VARCHAR(10) DEFAULT 'system',
    updated_at        TIMESTAMPTZ DEFAULT NOW()
);

-- 레시피 리뷰/평점
CREATE TABLE IF NOT EXISTS recipe_reviews (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipe_id       BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    rating          SMALLINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment         TEXT,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(user_id, recipe_id)
);
CREATE INDEX IF NOT EXISTS idx_recipe_reviews_recipe ON recipe_reviews(recipe_id);

-- 조리 이력
CREATE TABLE IF NOT EXISTS user_cooking_history (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipe_id       BIGINT REFERENCES recipes(id) ON DELETE SET NULL,
    cooked_at       TIMESTAMPTZ DEFAULT NOW(),
    used_ingredients BIGINT[] DEFAULT '{}'
);
CREATE INDEX IF NOT EXISTS idx_cooking_history_user ON user_cooking_history(user_id);

-- 디바이스 토큰
CREATE TABLE IF NOT EXISTS device_tokens (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token           VARCHAR(500) NOT NULL UNIQUE,
    device_type     VARCHAR(10) NOT NULL,
    is_active       BOOLEAN DEFAULT TRUE,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);
CREATE INDEX IF NOT EXISTS idx_device_tokens_user ON device_tokens(user_id);
