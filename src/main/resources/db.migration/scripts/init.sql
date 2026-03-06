-- liquibase formatted sql

-- changeset create tables:1

CREATE TABLE users (
    user_id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
);

CREATE TABLE cards (
    card_id BIGSERIAL PRIMARY KEY,
    number VARCHAR(255) NOT NULL UNIQUE,
    masked_number VARCHAR(255) NOT NULL,
    expired_at DATE NOT NULL,
    status VARCHAR(50) NOT NULL,
    balance NUMERIC(19, 2) NOT NULL,
    user_id BIGINT,
    FOREIGN KEY (user_id)
        REFERENCES users(user_id)
        ON DELETE SET NULL
);

CREATE TABLE transfers (
    transfer_id BIGSERIAL,
    from_card_id BIGINT,
    to_card_id BIGINT,
    amount DECIMAL NOT NULL,
    operation_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (from_card_id)
        REFERENCES cards(card_id),
    FOREIGN KEY (to_card_id)
        REFERENCES cards(card_id)
);

CREATE TABLE card_requests (
    request_id BIGSERIAL PRIMARY KEY,
    card_id BIGINT NOT NULL,
    requested_by BIGINT NOT NULL,
    request_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    processed_at TIMESTAMP,
    CONSTRAINT fk_card_requests_card
    FOREIGN KEY (card_id)
        REFERENCES cards (card_id)
            ON DELETE CASCADE,
            CONSTRAINT fk_card_requests_user
    FOREIGN KEY (requested_by)
        REFERENCES users (user_id)
        ON DELETE CASCADE
);