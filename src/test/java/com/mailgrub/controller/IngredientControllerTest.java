package com.mailgrub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailgrub.model.Ingredient;
import com.mailgrub.model.MeasurementType;
import com.mailgrub.repository.IngredientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IngredientRepository ingredientRepository;

    @BeforeEach
    void setup() {
        ingredientRepository.deleteAll();
    }

    @Test
    void testAddAndGetIngredient() throws Exception {
        Ingredient flour = new Ingredient();
        flour.setName("Flour");
        flour.setMeasurementType(MeasurementType.OZ);
        flour.setPurchaseSize(32.0);
        flour.setAverageCost(new BigDecimal("2.99"));

        mockMvc.perform(post("/ingredients/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(flour)))
                .andExpect(status().isOk())
                .andExpect(content().string("Ingredient Saved"));

        mockMvc.perform(get("/ingredients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Flour"));
    }

    @Test
    void testDeleteIngredient() throws Exception {
        Ingredient sugar = new Ingredient();
        sugar.setName("Sugar");
        sugar.setMeasurementType(MeasurementType.OZ);
        sugar.setPurchaseSize(16.0);
        sugar.setAverageCost(new BigDecimal("1.79"));

        Ingredient saved = ingredientRepository.save(sugar);

        mockMvc.perform(delete("/ingredients/delete/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string("Ingredient deleted"));
    }

    @Test
    void testUpdateIngredient() throws Exception {
        Ingredient bakingSoda = new Ingredient();
        bakingSoda.setName("Baking Soda");
        bakingSoda.setMeasurementType(MeasurementType.OZ);
        bakingSoda.setPurchaseSize(12.0);
        bakingSoda.setAverageCost(new BigDecimal("0.99"));

        Ingredient saved = ingredientRepository.save(bakingSoda);
        saved.setAverageCost(new BigDecimal("1.49"));

        mockMvc.perform(patch("/ingredients/update/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(saved)))
                .andExpect(status().isOk())
                .andExpect(content().string("Ingredient updated"));
    }
}
