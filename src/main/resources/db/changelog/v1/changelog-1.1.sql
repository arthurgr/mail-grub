-- liquibase formatted sql
-- changeset arevelski:1.1

CREATE TABLE recipe (
    id          SERIAL PRIMARY KEY NOT NULL,
    name        TEXT,
    items_made  INTEGER
);