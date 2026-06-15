CREATE INDEX idx_buyers_agency_status       ON buyers(agency_id, status);
CREATE INDEX idx_buyer_locations_buyer_id   ON buyer_locations(buyer_id);
CREATE INDEX idx_listings_agency_status     ON listings(agency_id, status);
CREATE INDEX idx_matches_listing_id         ON buyer_listing_matches(listing_id);
CREATE INDEX idx_matches_buyer_id           ON buyer_listing_matches(buyer_id);
CREATE INDEX idx_matches_agency_action      ON buyer_listing_matches(agency_id, agent_action);
