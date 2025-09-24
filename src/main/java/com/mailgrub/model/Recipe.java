package com.mailgrub.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Entity
@Table(name = "recipe")
public class Recipe {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "tenant_id", nullable = false, updatable = false)
  private String tenantId;

  private String name;
  private Integer itemsMade;

  @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<RecipeIngredient> recipeIngredients;

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getTenantId() {
    return tenantId;
  }

  public void setTenantId(String tenantId) {
    this.tenantId = tenantId;
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
    if (recipeIngredients == null) return BigDecimal.ZERO;
    return recipeIngredients.stream()
        .map(
            ri -> ri.getIngredient().getCostPerOunce().multiply(BigDecimal.valueOf(ri.getAmount())))
        .reduce(BigDecimal.ZERO, BigDecimal::add)
        .setScale(2, RoundingMode.HALF_UP);
  }

  public BigDecimal getCostPerItem() {
    if (itemsMade == null || itemsMade == 0) return BigDecimal.ZERO;
    return getTotalCost().divide(BigDecimal.valueOf(itemsMade), 2, RoundingMode.HALF_UP);
  }
}
