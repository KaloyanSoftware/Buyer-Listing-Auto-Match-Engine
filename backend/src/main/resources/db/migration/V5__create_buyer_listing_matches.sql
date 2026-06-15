CREATE TABLE buyer_listing_matches (
    id               UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    agency_id        UUID        NOT NULL REFERENCES agencies(id),
    buyer_id         UUID        NOT NULL REFERENCES buyers(id),
    listing_id       UUID        NOT NULL REFERENCES listings(id),
    match_score      INTEGER     NOT NULL,
    matched_criteria JSONB,
    agent_action     VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    notified_at      TIMESTAMPTZ,
    created_at       TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_match_buyer_listing UNIQUE (buyer_id, listing_id)
);
