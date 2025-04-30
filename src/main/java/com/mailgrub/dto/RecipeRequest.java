package com.mailgrub.dto;

import java.util.List;

public class RecipeRequest {
    private String name;
    private List<IngredientAmount> ingredients;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<IngredientAmount> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientAmount> ingredients) {
        this.ingredients = ingredients;
    }

    public static class IngredientAmount {
        private Integer ingredientId;
        private Double amount;

        public Integer getIngredientId() {
            return ingredientId;
        }

        public void setIngredientId(Integer ingredientId) {
            this.ingredientId = ingredientId;
        }

        public Double getAmount() {
            return amount;
        }

        public void setAmount(Double amount) {
            this.amount = amount;
        }
    }
}
