package com.mailgrub.dto;

import java.util.List;

public class RecipeRequest {
    private String name;
    private List<IngredientAmount> ingredients;
    private Integer itemsMade;

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

    public Integer getItemsMade() {
        return itemsMade;
    }

    public void setItemsMade(Integer itemsMade) {
        this.itemsMade = itemsMade;
    }

    public static class IngredientAmount {
        private Integer ingredientId;
        private Double amount;
        private String overrideMeasurementType;

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

        public String getOverrideMeasurementType() {
            return overrideMeasurementType;
        }

        public void setOverrideMeasurementType(String overrideMeasurementType) {
            this.overrideMeasurementType = overrideMeasurementType;
        }
    }
}
