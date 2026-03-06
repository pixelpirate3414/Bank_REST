-- liquibase formatted sql

-- changeset create tables:2

CREATE INDEX idx_cards_user_id
    ON cards(user_id);

CREATE INDEX idx_cards_expired_status
    ON cards(expired_at, status);

CREATE INDEX idx_transfers_from_card
    ON transfers(from_card_id);

CREATE INDEX idx_transfers_to_card
    ON transfers(to_card_id);

CREATE INDEX idx_card_requests_type
    ON card_requests(request_type);

CREATE INDEX idx_card_requests_card
    ON card_requests(card_id);

CREATE INDEX idx_card_requests_user
    ON card_requests(requested_by);