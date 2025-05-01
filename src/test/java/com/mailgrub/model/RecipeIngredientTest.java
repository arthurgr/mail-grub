package com.mailgrub.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeIngredientTest {

    @Test
    public void testGetTotalCost() {
        Ingredient ingredient = new Ingredient();
        ingredient.setAverageCost(new BigDecimal("1.00"));
        ingredient.setMeasurementType(MeasurementType.OZ);
        ingredient.setPurchaseSize(16.0); // 1.00 / 16 = 0.0625 per oz

        RecipeIngredient ri = new RecipeIngredient();
        ri.setIngredient(ingredient);
        ri.setAmount(2.0);

        BigDecimal expected = ingredient.getCostPerOunce().multiply(BigDecimal.valueOf(2.0)).setScale(2, BigDecimal.ROUND_HALF_UP);
        assertEquals(expected, ri.getTotalCost());
    }

    @Test
    public void testGetTotalCostWhenIngredientIsNull() {
        RecipeIngredient ri = new RecipeIngredient();
        ri.setAmount(2.0);
        assertEquals(BigDecimal.ZERO, ri.getTotalCost());
    }

    @Test
    public void testGetTotalCostWhenAmountIsNull() {
        Ingredient ingredient = new Ingredient();
        ingredient.setAverageCost(new BigDecimal("1.00"));
        ingredient.setMeasurementType(MeasurementType.OZ);
        ingredient.setPurchaseSize(16.0);

        RecipeIngredient ri = new RecipeIngredient();
        ri.setIngredient(ingredient);
        ri.setAmount(null);

        assertEquals(BigDecimal.ZERO, ri.getTotalCost());
    }
}
