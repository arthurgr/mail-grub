package com.mailgrub.model;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class PackagingTest {

    @Test
    void testCostPerUnitCalculation() {
        Packaging packaging = new Packaging();
        packaging.setAverageCost(new BigDecimal("10.00"));
        packaging.setQuantity(20);

        BigDecimal expected = new BigDecimal("0.50");
        assertEquals(0, expected.compareTo(packaging.getCostPerUnit()));
    }

    @Test
    void testCostPerUnitWithZeroQuantity() {
        Packaging packaging = new Packaging();
        packaging.setAverageCost(new BigDecimal("5.00"));
        packaging.setQuantity(0);

        assertEquals(BigDecimal.ZERO, packaging.getCostPerUnit());
    }

    @Test
    void testCostPerUnitWithNullsReturnsZero() {
        Packaging packaging = new Packaging();
        assertEquals(BigDecimal.ZERO, packaging.getCostPerUnit());
    }
}
