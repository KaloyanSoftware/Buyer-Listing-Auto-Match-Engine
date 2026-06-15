CREATE TABLE listings (
    id                  UUID          PRIMARY KEY DEFAULT gen_random_uuid(),
    agency_id           UUID          NOT NULL REFERENCES agencies(id),
    listed_by_agent_id  UUID          REFERENCES users(id),
    title               VARCHAR(300)  NOT NULL,
    price               DECIMAL(12,2) NOT NULL,
    property_type       VARCHAR(20)   NOT NULL,
    location            VARCHAR(200)  NOT NULL,
    sqm                 INTEGER,
    bedrooms            INTEGER,
    floor_number        INTEGER,
    total_floors        INTEGER,
    has_elevator        BOOLEAN       NOT NULL DEFAULT false,
    has_parking         BOOLEAN       NOT NULL DEFAULT false,
    has_garden          BOOLEAN       NOT NULL DEFAULT false,
    status              VARCHAR(20)   NOT NULL DEFAULT 'ACTIVE',
    description         TEXT,
    created_at          TIMESTAMPTZ   NOT NULL DEFAULT now()
);
