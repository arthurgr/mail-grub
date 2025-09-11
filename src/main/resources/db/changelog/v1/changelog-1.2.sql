-- liquibase formatted sql
-- changeset arevelski:1.2

CREATE TABLE recipe_ingredient (
    id                         SERIAL PRIMARY KEY NOT NULL,
    recipe_id                  INTEGER,
    ingredient_id              INTEGER,
    amount                     DOUBLE PRECISION,
    override_measurement_type  TEXT,
    CONSTRAINT fk_recipeingredient_recipe
        FOREIGN KEY (recipe_id)
        REFERENCES recipe (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_recipeingredient_ingredient
        FOREIGN KEY (ingredient_id)
        REFERENCES ingredient (id)
        ON DELETE CASCADE
);