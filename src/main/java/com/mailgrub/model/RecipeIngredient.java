package com.mailgrub.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
public class RecipeIngredient {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "recipe_id")
  private Recipe recipe;

  @ManyToOne
  @JoinColumn(name = "ingredient_id")
  private Ingredient ingredient;

  private Double amount;

  private String overrideMeasurementType; // New field

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Recipe getRecipe() {
    return recipe;
  }

  public void setRecipe(Recipe recipe) {
    this.recipe = recipe;
  }

  public Ingredient getIngredient() {
    return ingredient;
  }

  public void setIngredient(Ingredient ingredient) {
    this.ingredient = ingredient;
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

  public BigDecimal getTotalCost() {
    if (ingredient == null || amount == null) {
      return BigDecimal.ZERO;
    }

    return ingredient
        .getCostPerOunce()
        .multiply(BigDecimal.valueOf(amount))
        .setScale(2, RoundingMode.HALF_UP);
  }
}
