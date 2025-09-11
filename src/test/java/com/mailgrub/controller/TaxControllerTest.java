package com.mailgrub.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mailgrub.model.Tax;
import com.mailgrub.repository.TaxRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class TaxControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private TaxRepository taxRepository;

  @BeforeEach
  void setup() {
    taxRepository.deleteAll();

    Tax tax = new Tax();
    tax.setJurisdiction("Colorado");
    tax.setTaxRate(BigDecimal.valueOf(0.0825));
    taxRepository.save(tax);
  }

  @Test
  void testGetTaxes() throws Exception {
    mockMvc
        .perform(get("/taxes"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].jurisdiction").value("Colorado"));
  }

  @Test
  void testAddTax() throws Exception {
    Tax newTax = new Tax();
    newTax.setJurisdiction("Wyoming");
    newTax.setTaxRate(BigDecimal.valueOf(0.05));

    mockMvc
        .perform(
            post("/taxes/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(newTax)))
        .andExpect(status().isOk())
        .andExpect(content().string("Tax record saved"));
  }
}
