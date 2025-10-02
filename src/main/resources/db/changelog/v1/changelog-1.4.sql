-- liquibase formatted sql
-- changeset arevelski:1.4

CREATE TABLE taxes (
    id SERIAL PRIMARY KEY NOT NULL,
    tenant_id   TEXT NOT NULL,
    jurisdiction TEXT NOT NULL,
    tax_rate NUMERIC(5,2) NOT NULL
);