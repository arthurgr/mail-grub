package com.mailgrub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailgrub.dto.RecipeRequest;
import com.mailgrub.dto.RecipeRequest.IngredientAmount;
import com.mailgrub.model.Ingredient;
import com.mailgrub.model.MeasurementType;
import com.mailgrub.repository.IngredientRepository;
import com.mailgrub.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    private Ingredient savedFlour;
    private Ingredient savedSugar;

    @BeforeEach
    void setup() {
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();

        Ingredient flour = new Ingredient();
        flour.setName("Flour");
        flour.setMeasurementType(MeasurementType.OZ);
        flour.setPurchaseSize(32.0);
        flour.setAverageCost(new BigDecimal("2.99"));

        Ingredient sugar = new Ingredient();
        sugar.setName("Sugar");
        sugar.setMeasurementType(MeasurementType.OZ);
        sugar.setPurchaseSize(16.0);
        sugar.setAverageCost(new BigDecimal("1.79"));

        savedFlour = ingredientRepository.save(flour);
        savedSugar = ingredientRepository.save(sugar);
    }

    @Test
    void testAddAndGetRecipe() throws Exception {
        RecipeRequest request = new RecipeRequest();
        request.setName("Test Cookies");
        request.setItemsMade(12);

        IngredientAmount flourAmount = new IngredientAmount();
        flourAmount.setIngredientId(savedFlour.getId());
        flourAmount.setAmount(2.0);

        IngredientAmount sugarAmount = new IngredientAmount();
        sugarAmount.setIngredientId(savedSugar.getId());
        sugarAmount.setAmount(1.0);

        request.setIngredients(List.of(flourAmount, sugarAmount));

        mockMvc.perform(post("/recipes/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe saved"));

        mockMvc.perform(get("/recipes?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Test Cookies"))
                .andExpect(jsonPath("$.content[0].itemsMade").value(12))
                .andExpect(jsonPath("$.content[0].totalCost").exists())
                .andExpect(jsonPath("$.content[0].costPerItem").exists())
                .andExpect(jsonPath("$.content[0].ingredients.length()").value(2));
    }

    @Test
    void testAddRecipeMissingNameFails() throws Exception {
        RecipeRequest request = new RecipeRequest();
        request.setItemsMade(10);

        IngredientAmount flourAmount = new IngredientAmount();
        flourAmount.setIngredientId(savedFlour.getId());
        flourAmount.setAmount(2.0);

        request.setIngredients(List.of(flourAmount));

        mockMvc.perform(post("/recipes/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Recipe name is required"));
    }
}
