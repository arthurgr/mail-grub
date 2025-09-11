package com.mailgrub.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

public class TaxTest {

  @Test
  public void testTaxModel() {
    Tax tax = new Tax();
    tax.setId(1);
    tax.setJurisdiction("Colorado");
    tax.setTaxRate(BigDecimal.valueOf(7.25));

    assertEquals(1, tax.getId());
    assertEquals("Colorado", tax.getJurisdiction());
    assertEquals(BigDecimal.valueOf(7.25), tax.getTaxRate());
  }
}
