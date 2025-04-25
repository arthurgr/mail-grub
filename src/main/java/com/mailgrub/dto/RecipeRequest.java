package com.mailgrub.dto;

import java.util.List;

public class RecipeRequest {
    private String name;
    private List<Integer> ingredientIds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getIngredientIds() {
        return ingredientIds;
    }

    public void setIngredientIds(List<Integer> ingredientIds) {
        this.ingredientIds = ingredientIds;
    }
}
