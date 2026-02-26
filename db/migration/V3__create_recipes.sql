-- 레시피
CREATE TABLE recipes (
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

CREATE INDEX idx_recipes_cuisine ON recipes(cuisine_type);
CREATE INDEX idx_recipes_tags ON recipes USING GIN(tags);

-- 레시피 필요 재료
CREATE TABLE recipe_ingredients (
    id              BIGSERIAL PRIMARY KEY,
    recipe_id       BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    ingredient_id   BIGINT NOT NULL REFERENCES ingredient_master(id),
    quantity        VARCHAR(50),
    is_essential    BOOLEAN DEFAULT TRUE,
    substitute_ids  BIGINT[] DEFAULT '{}',
    UNIQUE(recipe_id, ingredient_id)
);

CREATE INDEX idx_recipe_ingredients_recipe ON recipe_ingredients(recipe_id);
CREATE INDEX idx_recipe_ingredients_ingredient ON recipe_ingredients(ingredient_id);
