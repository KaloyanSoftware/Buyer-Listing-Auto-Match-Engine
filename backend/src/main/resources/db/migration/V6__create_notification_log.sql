CREATE TABLE notification_log (
    id                UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    agency_id         UUID        REFERENCES agencies(id),
    recipient_user_id UUID        REFERENCES users(id),
    listing_id        UUID        REFERENCES listings(id),
    match_count       INTEGER,
    channel           VARCHAR(20),
    status            VARCHAR(20),
    sent_at           TIMESTAMPTZ NOT NULL DEFAULT now()
);
