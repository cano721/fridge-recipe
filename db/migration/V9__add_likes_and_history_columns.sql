-- 레시피 좋아요
CREATE TABLE recipe_likes (
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    recipe_id   BIGINT NOT NULL REFERENCES recipes(id) ON DELETE CASCADE,
    created_at  TIMESTAMPTZ DEFAULT NOW(),
    PRIMARY KEY (user_id, recipe_id)
);

CREATE INDEX idx_recipe_likes_recipe ON recipe_likes(recipe_id);

-- 조리 이력에 평점/메모 추가
ALTER TABLE user_cooking_history
    ADD COLUMN IF NOT EXISTS rating SMALLINT CHECK (rating BETWEEN 1 AND 5),
    ADD COLUMN IF NOT EXISTS memo TEXT;
