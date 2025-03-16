package com.example.restservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(IngredientController.class) // Loads only the web layer
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testGetIngredient() throws Exception {
        mockMvc.perform(get("/ingredient").param("name", "Sugar"))
                .andExpect(status().isOk()) // Check if HTTP 200 OK is returned
                .andExpect(jsonPath("$.id").exists()) // Check if 'id' exists in JSON response
                .andExpect(jsonPath("$.content").value("Hello, Sugar!")); // Check if the response content is as expected
    }
}