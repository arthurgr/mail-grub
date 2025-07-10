package com.mailgrub.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaxTest {

    @Test
    public void testTaxModel() {
        Tax tax = new Tax();
        tax.setId(1);
        tax.setJurisdiction("Colorado");
        tax.setTaxRate(7.25);

        assertEquals(1, tax.getId());
        assertEquals("Colorado", tax.getJurisdiction());
        assertEquals(7.25, tax.getTaxRate());
    }
}
