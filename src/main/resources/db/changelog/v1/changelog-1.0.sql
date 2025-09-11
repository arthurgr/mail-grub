-- liquibase formatted sql
-- changeset arevelski:1.0

CREATE TABLE ingredient (
    id              SERIAL PRIMARY KEY NOT NULL,
    name            TEXT,
    measurement_type TEXT,
    purchase_size   DOUBLE PRECISION,
    average_cost    NUMERIC(10,2)
);