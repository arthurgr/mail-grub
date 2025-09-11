-- liquibase formatted sql
-- changeset arevelski:1.4

CREATE TABLE taxes (
    id SERIAL PRIMARY KEY NOT NULL,
    jurisdiction TEXT NOT NULL,
    tax_rate NUMERIC(5,4) NOT NULL
);