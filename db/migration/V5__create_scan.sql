-- 스캔 이력
CREATE TABLE scan_history (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    scan_type   VARCHAR(20) NOT NULL,
    image_url   VARCHAR(500),
    status      VARCHAR(20) DEFAULT 'processing',
    result      JSONB,
    created_at  TIMESTAMPTZ DEFAULT NOW()
);

CREATE INDEX idx_scan_history_user ON scan_history(user_id);
