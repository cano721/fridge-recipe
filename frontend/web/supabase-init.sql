-- =====================================================
-- Fridge Recipe - Supabase 초기화 SQL
-- Supabase SQL Editor에서 실행하세요
-- =====================================================

-- 1. Extensions
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- 2. Tables
CREATE TABLE IF NOT EXISTS users (
    id              BIGSERIAL PRIMARY KEY,
    email           VARCHAR(255),
    nickname        VARCHAR(50) NOT NULL,
    profile_image   VARCHAR(500),
    oauth_provider  VARCHAR(20) NOT NULL,
    oauth_id        VARCHAR(255) NOT NULL,
    dietary_prefs   JSONB DEFAULT '{}',
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(oauth_provider, oauth_id)
);

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
    expired_notified BOOLEAN DEFAULT FALSE,
    created_at      TIMESTAMPTZ DEFAULT NOW(),
    updated_at      TIMESTAMPTZ DEFAULT NOW()
);

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
    steps           JSONB NOT NULL,
    nutrition       JSONB,
    tags            TEXT[] DEFAULT '{}',
    view_count      INT DEFAULT 0,
    avg_rating      DECIMAL(2,1) DEFAULT 0,
    source_url      VARCHAR(500),
    source_type     VARCHAR(20) NOT NULL DEFAULT 'manual',
    created_at      TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS recipe_ingredients (
    id              BIGSERIAL PRIMARY KEY,
    recipe_id       BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    ingredient_id   BIGINT NOT NULL REFERENCES ingredient_master(id),
    quantity        VARCHAR(50),
    is_essential    BOOLEAN DEFAULT TRUE,
    substitute_ids  BIGINT[] DEFAULT '{}',
    UNIQUE(recipe_id, ingredient_id)
);

CREATE TABLE IF NOT EXISTS bookmarks (
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipe_id   BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    created_at  TIMESTAMPTZ DEFAULT NOW(),
    PRIMARY KEY (user_id, recipe_id)
);

CREATE TABLE IF NOT EXISTS recipe_reviews (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipe_id   BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    rating      SMALLINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment     TEXT,
    created_at  TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(user_id, recipe_id)
);

CREATE TABLE IF NOT EXISTS user_cooking_history (
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipe_id           BIGINT REFERENCES recipes(id) ON DELETE SET NULL,
    cooked_at           TIMESTAMPTZ DEFAULT NOW(),
    used_ingredients    BIGINT[] DEFAULT '{}'
);

CREATE TABLE IF NOT EXISTS scan_history (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    scan_type   VARCHAR(20) NOT NULL,
    image_url   VARCHAR(500),
    status      VARCHAR(20) DEFAULT 'processing',
    result      JSONB,
    created_at  TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS notification_settings (
    user_id             BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    expiry_enabled      BOOLEAN DEFAULT TRUE,
    expiry_days         INT[] DEFAULT '{3, 1, 0}',
    theme_preference    VARCHAR(10) DEFAULT 'system',
    updated_at          TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS device_tokens (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token       VARCHAR(500) NOT NULL UNIQUE,
    device_type VARCHAR(10) NOT NULL,
    is_active   BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMPTZ DEFAULT NOW(),
    updated_at  TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE IF NOT EXISTS refresh_tokens (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash  VARCHAR(64) NOT NULL UNIQUE,
    device_info VARCHAR(200),
    expires_at  TIMESTAMPTZ NOT NULL,
    created_at  TIMESTAMPTZ DEFAULT NOW(),
    revoked_at  TIMESTAMPTZ
);

-- 3. Indexes
CREATE INDEX IF NOT EXISTS idx_user_ingredients_user ON user_ingredients(user_id);
CREATE INDEX IF NOT EXISTS idx_user_ingredients_expiry ON user_ingredients(user_id, expiry_date);
CREATE UNIQUE INDEX IF NOT EXISTS idx_user_ingredients_dedup
    ON user_ingredients(user_id, ingredient_id, storage_type, COALESCE(expiry_date, DATE '9999-12-31'));
CREATE INDEX IF NOT EXISTS idx_recipes_cuisine ON recipes(cuisine_type);
CREATE INDEX IF NOT EXISTS idx_recipes_tags ON recipes USING GIN(tags);
CREATE INDEX IF NOT EXISTS idx_recipe_ingredients_recipe ON recipe_ingredients(recipe_id);
CREATE INDEX IF NOT EXISTS idx_recipe_ingredients_ingredient ON recipe_ingredients(ingredient_id);
CREATE INDEX IF NOT EXISTS idx_recipe_reviews_recipe ON recipe_reviews(recipe_id);
CREATE INDEX IF NOT EXISTS idx_cooking_history_user ON user_cooking_history(user_id);
CREATE INDEX IF NOT EXISTS idx_scan_history_user ON scan_history(user_id);
CREATE INDEX IF NOT EXISTS idx_device_tokens_user ON device_tokens(user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_user ON refresh_tokens(user_id);
CREATE INDEX IF NOT EXISTS idx_refresh_tokens_expires ON refresh_tokens(expires_at) WHERE revoked_at IS NULL;
CREATE INDEX IF NOT EXISTS idx_ingredient_master_aliases ON ingredient_master USING GIN(aliases);
CREATE INDEX IF NOT EXISTS idx_ingredient_master_trgm ON ingredient_master USING GIN(name gin_trgm_ops);
CREATE INDEX IF NOT EXISTS idx_recipes_title_trgm ON recipes USING GIN(title gin_trgm_ops);

-- 4. RLS (Row Level Security) - 서비스 키 사용 시 바이패스
ALTER TABLE users ENABLE ROW LEVEL SECURITY;
ALTER TABLE user_ingredients ENABLE ROW LEVEL SECURITY;
ALTER TABLE bookmarks ENABLE ROW LEVEL SECURITY;
ALTER TABLE refresh_tokens ENABLE ROW LEVEL SECURITY;

-- 5. Seed Data: 식재료 마스터
INSERT INTO ingredient_master (name, category, icon_url, default_unit, default_expiry_days, aliases) VALUES
-- 채소류
('양파', '채소', NULL, '개', 30, '{"어니언"}'),
('대파', '채소', NULL, '대', 14, '{"파"}'),
('마늘', '채소', NULL, '쪽', 60, '{"깐마늘"}'),
('감자', '채소', NULL, '개', 30, '{"potato"}'),
('당근', '채소', NULL, '개', 21, '{"carrot"}'),
('배추', '채소', NULL, '포기', 14, '{"김장배추"}'),
('시금치', '채소', NULL, 'g', 5, '{"뽀빠이"}'),
('버섯', '채소', NULL, 'g', 7, '{"팽이버섯","새송이","표고"}'),
('고추', '채소', NULL, '개', 14, '{"청양고추","풋고추"}'),
('토마토', '채소', NULL, '개', 7, '{"방울토마토"}'),
('양배추', '채소', NULL, '개', 14, '{"cabbage"}'),
('브로콜리', '채소', NULL, '개', 7, '{"broccoli"}'),
('콩나물', '채소', NULL, 'g', 3, '{}'),
('애호박', '채소', NULL, '개', 7, '{"호박"}'),
('오이', '채소', NULL, '개', 7, '{"cucumber"}'),
-- 육류
('돼지고기', '육류', NULL, 'g', 3, '{"삼겹살","목살","돼지"}'),
('소고기', '육류', NULL, 'g', 3, '{"한우","beef"}'),
('닭고기', '육류', NULL, 'g', 2, '{"치킨","닭가슴살","닭"}'),
('계란', '육류', NULL, '개', 21, '{"달걀","egg"}'),
('베이컨', '육류', NULL, 'g', 14, '{"bacon"}'),
-- 해산물
('연어', '해산물', NULL, 'g', 2, '{"salmon"}'),
('새우', '해산물', NULL, 'g', 2, '{"shrimp"}'),
('참치캔', '해산물', NULL, '캔', 365, '{"참치"}'),
('오징어', '해산물', NULL, 'g', 2, '{"squid"}'),
('멸치', '해산물', NULL, 'g', 180, '{"anchovy"}'),
-- 유제품
('우유', '유제품', NULL, 'ml', 7, '{"milk"}'),
('치즈', '유제품', NULL, 'g', 30, '{"cheese","모짜렐라","체다"}'),
('버터', '유제품', NULL, 'g', 60, '{"butter"}'),
('요거트', '유제품', NULL, 'ml', 14, '{"yogurt"}'),
('생크림', '유제품', NULL, 'ml', 7, '{"cream"}'),
-- 양념/소스
('간장', '양념', NULL, 'ml', 365, '{"소이소스","soy sauce"}'),
('고추장', '양념', NULL, 'g', 365, '{"gochujang"}'),
('된장', '양념', NULL, 'g', 365, '{"doenjang"}'),
('식용유', '양념', NULL, 'ml', 365, '{"기름"}'),
('참기름', '양념', NULL, 'ml', 180, '{"sesame oil"}'),
('소금', '양념', NULL, 'g', 1095, '{"salt"}'),
('설탕', '양념', NULL, 'g', 730, '{"sugar"}'),
('후추', '양념', NULL, 'g', 730, '{"pepper"}'),
('식초', '양념', NULL, 'ml', 365, '{"vinegar"}'),
('굴소스', '양념', NULL, 'ml', 365, '{"oyster sauce"}'),
-- 곡물/면
('쌀', '곡물', NULL, 'g', 180, '{"rice"}'),
('국수', '곡물', NULL, 'g', 365, '{"소면","noodle"}'),
('라면', '곡물', NULL, '봉지', 180, '{"ramen"}'),
('파스타', '곡물', NULL, 'g', 365, '{"스파게티","pasta"}'),
('밀가루', '곡물', NULL, 'g', 365, '{"flour"}'),
-- 과일
('사과', '과일', NULL, '개', 14, '{"apple"}'),
('바나나', '과일', NULL, '개', 5, '{"banana"}'),
('레몬', '과일', NULL, '개', 21, '{"lemon"}'),
('귤', '과일', NULL, '개', 14, '{"mandarin"}'),
('딸기', '과일', NULL, 'g', 3, '{"strawberry"}')
ON CONFLICT (name) DO NOTHING;

-- 6. Seed Data: 샘플 레시피
INSERT INTO recipes (title, description, cuisine_type, difficulty, cooking_time, servings, calories, steps, nutrition, tags) VALUES
('김치볶음밥', '남은 밥과 김치로 만드는 간단한 볶음밥', '한식', 'easy', 15, 1, 450,
 '[{"step":1,"description":"김치를 잘게 썰어주세요","timer":null},{"step":2,"description":"팬에 기름을 두르고 김치를 볶아주세요","timer":180},{"step":3,"description":"밥을 넣고 함께 볶아주세요","timer":120},{"step":4,"description":"계란후라이를 올려 완성합니다","timer":60}]',
 '{"protein":15,"carbs":65,"fat":12,"fiber":3}',
 '{"간단요리","한그릇","볶음밥"}'),

('토마토 파스타', '신선한 토마토로 만드는 클래식 파스타', '양식', 'easy', 25, 2, 520,
 '[{"step":1,"description":"파스타를 삶아주세요 (알덴테)","timer":480},{"step":2,"description":"마늘을 올리브오일에 볶아주세요","timer":60},{"step":3,"description":"토마토를 넣고 소스를 만들어주세요","timer":300},{"step":4,"description":"삶은 파스타를 소스에 버무려주세요","timer":60}]',
 '{"protein":18,"carbs":72,"fat":15,"fiber":4}',
 '{"파스타","토마토","양식"}'),

('된장찌개', '구수한 된장으로 끓이는 한국 전통 찌개', '한식', 'easy', 30, 3, 180,
 '[{"step":1,"description":"멸치육수를 끓여주세요","timer":600},{"step":2,"description":"된장을 풀어주세요","timer":60},{"step":3,"description":"감자, 호박, 두부를 넣어주세요","timer":300},{"step":4,"description":"대파, 고추를 넣고 마무리합니다","timer":120}]',
 '{"protein":12,"carbs":15,"fat":6,"fiber":5}',
 '{"찌개","된장","전통"}'),

('닭가슴살 샐러드', '다이어트에 좋은 고단백 샐러드', '양식', 'easy', 20, 1, 280,
 '[{"step":1,"description":"닭가슴살을 삶거나 구워주세요","timer":600},{"step":2,"description":"채소를 씻고 먹기 좋게 잘라주세요","timer":180},{"step":3,"description":"닭가슴살을 슬라이스합니다","timer":60},{"step":4,"description":"드레싱을 뿌려 완성합니다","timer":30}]',
 '{"protein":35,"carbs":12,"fat":8,"fiber":6}',
 '{"다이어트","샐러드","고단백"}'),

('계란말이', '부드러운 계란말이', '한식', 'medium', 15, 2, 200,
 '[{"step":1,"description":"계란을 풀고 소금, 파를 넣어주세요","timer":60},{"step":2,"description":"팬에 기름을 두르고 약불로 줄여주세요","timer":30},{"step":3,"description":"계란물을 얇게 펴고 말아주세요","timer":120},{"step":4,"description":"먹기 좋게 잘라 완성합니다","timer":30}]',
 '{"protein":14,"carbs":2,"fat":12,"fiber":0}',
 '{"반찬","계란","간단요리"}'),

('소고기 미역국', '생일이나 특별한 날의 미역국', '한식', 'medium', 40, 4, 220,
 '[{"step":1,"description":"미역을 불려주세요 (30분)","timer":1800},{"step":2,"description":"소고기를 참기름에 볶아주세요","timer":180},{"step":3,"description":"물을 넣고 끓여주세요","timer":600},{"step":4,"description":"간장으로 간을 맞춰 완성합니다","timer":60}]',
 '{"protein":20,"carbs":8,"fat":10,"fiber":3}',
 '{"국","미역","소고기","생일"}'),

('새우볶음밥', '탱글탱글 새우가 들어간 볶음밥', '중식', 'easy', 20, 2, 480,
 '[{"step":1,"description":"새우 손질하기","timer":180},{"step":2,"description":"채소를 잘게 다져주세요","timer":120},{"step":3,"description":"새우와 채소를 볶아주세요","timer":180},{"step":4,"description":"밥을 넣고 센불에 볶아 완성합니다","timer":120}]',
 '{"protein":22,"carbs":60,"fat":14,"fiber":2}',
 '{"볶음밥","새우","중식"}'),

('감자조림', '짭짤 달콤한 밑반찬', '한식', 'easy', 25, 4, 160,
 '[{"step":1,"description":"감자 껍질 벗기고 깍둑썰기","timer":180},{"step":2,"description":"간장 양념장 만들기","timer":60},{"step":3,"description":"감자를 양념장에 졸여주세요","timer":600},{"step":4,"description":"윤기 나게 마무리합니다","timer":120}]',
 '{"protein":3,"carbs":35,"fat":2,"fiber":4}',
 '{"반찬","감자","밑반찬"}')
ON CONFLICT DO NOTHING;

-- 7. Seed Data: 레시피-식재료 연결
-- 김치볶음밥 (id=1)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential) VALUES
(1, (SELECT id FROM ingredient_master WHERE name='쌀'), '1공기', true),
(1, (SELECT id FROM ingredient_master WHERE name='계란'), '1개', false),
(1, (SELECT id FROM ingredient_master WHERE name='식용유'), '1큰술', true),
(1, (SELECT id FROM ingredient_master WHERE name='참기름'), '약간', false)
ON CONFLICT DO NOTHING;

-- 토마토 파스타 (id=2)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential) VALUES
(2, (SELECT id FROM ingredient_master WHERE name='파스타'), '200g', true),
(2, (SELECT id FROM ingredient_master WHERE name='토마토'), '3개', true),
(2, (SELECT id FROM ingredient_master WHERE name='마늘'), '4쪽', true),
(2, (SELECT id FROM ingredient_master WHERE name='식용유'), '3큰술', true),
(2, (SELECT id FROM ingredient_master WHERE name='소금'), '약간', true)
ON CONFLICT DO NOTHING;

-- 된장찌개 (id=3)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential) VALUES
(3, (SELECT id FROM ingredient_master WHERE name='된장'), '2큰술', true),
(3, (SELECT id FROM ingredient_master WHERE name='감자'), '1개', false),
(3, (SELECT id FROM ingredient_master WHERE name='애호박'), '1/2개', false),
(3, (SELECT id FROM ingredient_master WHERE name='대파'), '1대', false),
(3, (SELECT id FROM ingredient_master WHERE name='고추'), '1개', false),
(3, (SELECT id FROM ingredient_master WHERE name='멸치'), '10g', true)
ON CONFLICT DO NOTHING;

-- 닭가슴살 샐러드 (id=4)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential) VALUES
(4, (SELECT id FROM ingredient_master WHERE name='닭고기'), '200g', true),
(4, (SELECT id FROM ingredient_master WHERE name='토마토'), '1개', false),
(4, (SELECT id FROM ingredient_master WHERE name='양파'), '1/4개', false),
(4, (SELECT id FROM ingredient_master WHERE name='레몬'), '1/2개', false)
ON CONFLICT DO NOTHING;

-- 계란말이 (id=5)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential) VALUES
(5, (SELECT id FROM ingredient_master WHERE name='계란'), '4개', true),
(5, (SELECT id FROM ingredient_master WHERE name='대파'), '1/2대', false),
(5, (SELECT id FROM ingredient_master WHERE name='당근'), '1/4개', false),
(5, (SELECT id FROM ingredient_master WHERE name='소금'), '약간', true),
(5, (SELECT id FROM ingredient_master WHERE name='식용유'), '약간', true)
ON CONFLICT DO NOTHING;

-- 소고기 미역국 (id=6)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential) VALUES
(6, (SELECT id FROM ingredient_master WHERE name='소고기'), '150g', true),
(6, (SELECT id FROM ingredient_master WHERE name='참기름'), '1큰술', true),
(6, (SELECT id FROM ingredient_master WHERE name='간장'), '2큰술', true),
(6, (SELECT id FROM ingredient_master WHERE name='마늘'), '2쪽', false)
ON CONFLICT DO NOTHING;

-- 새우볶음밥 (id=7)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential) VALUES
(7, (SELECT id FROM ingredient_master WHERE name='새우'), '150g', true),
(7, (SELECT id FROM ingredient_master WHERE name='쌀'), '2공기', true),
(7, (SELECT id FROM ingredient_master WHERE name='계란'), '2개', false),
(7, (SELECT id FROM ingredient_master WHERE name='양파'), '1/2개', false),
(7, (SELECT id FROM ingredient_master WHERE name='당근'), '1/4개', false),
(7, (SELECT id FROM ingredient_master WHERE name='대파'), '1대', false)
ON CONFLICT DO NOTHING;

-- 감자조림 (id=8)
INSERT INTO recipe_ingredients (recipe_id, ingredient_id, quantity, is_essential) VALUES
(8, (SELECT id FROM ingredient_master WHERE name='감자'), '3개', true),
(8, (SELECT id FROM ingredient_master WHERE name='간장'), '3큰술', true),
(8, (SELECT id FROM ingredient_master WHERE name='설탕'), '1큰술', true),
(8, (SELECT id FROM ingredient_master WHERE name='식용유'), '1큰술', true),
(8, (SELECT id FROM ingredient_master WHERE name='마늘'), '2쪽', false)
ON CONFLICT DO NOTHING;
