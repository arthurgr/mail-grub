package com.mailgrub.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Entity
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    private Integer itemsMade;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<RecipeIngredient> recipeIngredients;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getItemsMade() {
        return itemsMade;
    }

    public void setItemsMade(Integer itemsMade) {
        this.itemsMade = itemsMade;
    }

    public List<RecipeIngredient> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<RecipeIngredient> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }

    public BigDecimal getTotalCost() {
        return recipeIngredients.stream()
                .map(RecipeIngredient::getTotalCost)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getCostPerItem() {
        if (itemsMade == null || itemsMade == 0) return BigDecimal.ZERO;
        return getTotalCost().divide(BigDecimal.valueOf(itemsMade), 2, RoundingMode.HALF_UP);
    }
}
