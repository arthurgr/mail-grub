package com.mailgrub.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class IngredientTest {

    @Test
    void testCostPerOunceForOunces() {
        Ingredient ingredient = new Ingredient();
        ingredient.setMeasurementType(MeasurementType.OZ);
        ingredient.setPurchaseSize(8.0);
        ingredient.setAverageCost(new BigDecimal("4.00"));

        BigDecimal expected = new BigDecimal("0.50");
        assertEquals(0, expected.compareTo(ingredient.getCostPerOunce()));
    }

    @Test
    void testCostPerOunceForPounds() {
        Ingredient ingredient = new Ingredient();
        ingredient.setMeasurementType(MeasurementType.LB);
        ingredient.setPurchaseSize(1.0);
        ingredient.setAverageCost(new BigDecimal("3.20"));

        BigDecimal expected = new BigDecimal("0.20"); // 3.20 / 16
        assertEquals(0, expected.compareTo(ingredient.getCostPerOunce()));
    }

    @Test
    void testCostPerOunceForKilograms() {
        Ingredient ingredient = new Ingredient();
        ingredient.setMeasurementType(MeasurementType.KG);
        ingredient.setPurchaseSize(1.0);
        ingredient.setAverageCost(new BigDecimal("7.00"));

        BigDecimal expected = new BigDecimal("0.20"); // 7.00 / 35.274 = ~0.1985 → 0.20
        assertEquals(0, expected.compareTo(ingredient.getCostPerOunce()));
    }

    @Test
    void testCostPerOunceWithNullFieldsReturnsZero() {
        Ingredient ingredient = new Ingredient();
        assertEquals(BigDecimal.ZERO, ingredient.getCostPerOunce());
    }
}
