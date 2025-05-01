package com.mailgrub.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RecipeTest {

    @Test
    public void testGetTotalCost() {
        Ingredient flour = new Ingredient();
        flour.setAverageCost(new BigDecimal("2.00"));
        flour.setMeasurementType(MeasurementType.OZ);
        flour.setPurchaseSize(32.0);

        RecipeIngredient ri1 = new RecipeIngredient();
        ri1.setIngredient(flour);
        ri1.setAmount(2.0);

        Recipe recipe = new Recipe();
        recipe.setRecipeIngredients(List.of(ri1));

        BigDecimal expected = flour.getCostPerOunce().multiply(BigDecimal.valueOf(2.0));
        assertEquals(0, expected.compareTo(recipe.getTotalCost()));
    }

    @Test
    public void testGetCostPerItem() {
        Recipe recipe = new Recipe();
        recipe.setItemsMade(10);

        Ingredient sugar = new Ingredient();
        sugar.setAverageCost(new BigDecimal("1.00"));
        sugar.setMeasurementType(MeasurementType.OZ);
        sugar.setPurchaseSize(10.0);

        RecipeIngredient ri = new RecipeIngredient();
        ri.setIngredient(sugar);
        ri.setAmount(2.0);
        ri.setRecipe(recipe);

        recipe.setRecipeIngredients(List.of(ri));

        BigDecimal expected = sugar.getCostPerOunce().multiply(BigDecimal.valueOf(2.0)).divide(BigDecimal.valueOf(10), 2, BigDecimal.ROUND_HALF_UP);
        assertEquals(expected, recipe.getCostPerItem());
    }

    @Test
    public void testCostPerItemZeroItemsMade() {
        Recipe recipe = new Recipe();
        recipe.setItemsMade(0);
        assertEquals(BigDecimal.ZERO, recipe.getCostPerItem());
    }
}
