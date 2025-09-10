-- liquibase formatted sql
-- changeset arevelski:1.1

CREATE TABLE packaging (
    id SERIAL PRIMARY KEY NOT NULL,
    packaging_materials TEXT,
    average_cost NUMERIC(10,2),
    quantity INTEGER,
    cost_per_unit NUMERIC(10,2),
    procurement TEXT
);
