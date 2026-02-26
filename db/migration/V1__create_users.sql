-- pg_trgm 확장 (유사도 검색용)
CREATE EXTENSION IF NOT EXISTS pg_trgm;

-- 사용자
CREATE TABLE users (
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
