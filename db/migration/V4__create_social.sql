-- 즐겨찾기
CREATE TABLE bookmarks (
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipe_id   BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    created_at  TIMESTAMPTZ DEFAULT NOW(),
    PRIMARY KEY (user_id, recipe_id)
);

-- 레시피 리뷰/평점
CREATE TABLE recipe_reviews (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipe_id   BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    rating      SMALLINT NOT NULL CHECK (rating BETWEEN 1 AND 5),
    comment     TEXT,
    created_at  TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(user_id, recipe_id)
);

CREATE INDEX idx_recipe_reviews_recipe ON recipe_reviews(recipe_id);

-- 조리 이력
CREATE TABLE user_cooking_history (
    id                  BIGSERIAL PRIMARY KEY,
    user_id             BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipe_id           BIGINT REFERENCES recipes(id) ON DELETE SET NULL,
    cooked_at           TIMESTAMPTZ DEFAULT NOW(),
    used_ingredients    BIGINT[] DEFAULT '{}'
);

CREATE INDEX idx_cooking_history_user ON user_cooking_history(user_id);
