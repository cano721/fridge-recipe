-- 알림 설정
CREATE TABLE notification_settings (
    user_id             BIGINT PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    expiry_enabled      BOOLEAN DEFAULT TRUE,
    expiry_days         INT[] DEFAULT '{3, 1, 0}',
    theme_preference    VARCHAR(10) DEFAULT 'system',
    updated_at          TIMESTAMPTZ DEFAULT NOW()
);

-- 디바이스 토큰 (푸시 알림용)
CREATE TABLE device_tokens (
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token       VARCHAR(500) NOT NULL,
    device_type VARCHAR(10) NOT NULL,
    is_active   BOOLEAN DEFAULT TRUE,
    created_at  TIMESTAMPTZ DEFAULT NOW(),
    updated_at  TIMESTAMPTZ DEFAULT NOW(),
    UNIQUE(token)
);

CREATE INDEX idx_device_tokens_user ON device_tokens(user_id);
