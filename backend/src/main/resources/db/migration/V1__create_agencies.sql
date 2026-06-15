CREATE TABLE agencies (
    id           UUID        PRIMARY KEY DEFAULT gen_random_uuid(),
    name         VARCHAR(200) NOT NULL,
    country_code CHAR(2),
    created_at   TIMESTAMPTZ NOT NULL DEFAULT now()
);
