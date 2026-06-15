CREATE TABLE buyers (
    id                  UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
    agency_id           UUID          NOT NULL REFERENCES agencies(id),
    assigned_agent_id   UUID          REFERENCES users(id),
    full_name           VARCHAR(200)  NOT NULL,
    email               VARCHAR(320),
    phone               VARCHAR(30),
    status              VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
    budget_min          DECIMAL(12,2),
    budget_max          DECIMAL(12,2) NOT NULL,
    property_type       VARCHAR(20)   NOT NULL,
    min_sqm             INTEGER,
    min_bedrooms        INTEGER,
    requires_elevator   BOOLEAN       NOT NULL DEFAULT false,
    requires_parking    BOOLEAN       NOT NULL DEFAULT false,
    requires_garden     BOOLEAN       NOT NULL DEFAULT false,
    avoid_ground_floor  BOOLEAN       NOT NULL DEFAULT false,
    avoid_top_floor     BOOLEAN       NOT NULL DEFAULT false,
    timeline            VARCHAR(30),
    notes               TEXT,
    created_at          TIMESTAMPTZ   NOT NULL DEFAULT now(),
    last_contacted_at   TIMESTAMPTZ
);

CREATE TABLE buyer_locations (
    id            UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    buyer_id      UUID         NOT NULL REFERENCES buyers(id) ON DELETE CASCADE,
    location_name VARCHAR(200) NOT NULL
);
