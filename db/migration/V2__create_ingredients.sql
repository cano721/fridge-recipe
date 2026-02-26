-- 식재료 마스터 (관리자 관리)
CREATE TABLE ingredient_master (
    id                  BIGSERIAL PRIMARY KEY,
    name                VARCHAR(100) NOT NULL UNIQUE,
    category            VARCHAR(50) NOT NULL,
    icon_url            VARCHAR(500),
    default_unit        VARCHAR(20),
    default_expiry_days INT,
    aliases             TEXT[] DEFAULT '{}',
    created_at          TIMESTAMPTZ DEFAULT NOW()
);

-- 사용자 냉장고 식재료
CREATE TABLE user_ingredients (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    ingredient_id   BIGINT NOT NULL REFERENCES ingredient_master(id),
    quantity        DECIMAL(10,2),
    unit            VARCHAR(20),
    expiry_date     DATE,
    storage_type    VARCHAR(10) DEFAULT 'fridge',
    memo            VARCHAR(200),
    registered_via  VARCHAR(20) DEFAULT 'manual',
    expired_notified BOOLEAN DEFAULT FALSE,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_user_ingredients_user ON user_ingredients(user_id);
CREATE INDEX idx_user_ingredients_expiry ON user_ingredients(user_id, expiry_date);

-- 간편 등록 중복 방지
CREATE UNIQUE INDEX idx_user_ingredients_dedup
    ON user_ingredients(user_id, ingredient_id, storage_type, COALESCE(expiry_date, DATE '9999-12-31'));
