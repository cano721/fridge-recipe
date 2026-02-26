-- 추가 인덱스
CREATE INDEX idx_ingredient_master_aliases ON ingredient_master USING GIN(aliases);
CREATE INDEX idx_ingredient_master_trgm ON ingredient_master USING GIN(name gin_trgm_ops);
CREATE INDEX idx_recipes_title_trgm ON recipes USING GIN(title gin_trgm_ops);
