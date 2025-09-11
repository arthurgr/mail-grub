package com.mailgrub.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mailgrub.model.Packaging;
import com.mailgrub.repository.PackagingRepository;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class PackagingControllerTest {

  @Autowired private MockMvc mockMvc;

  @Autowired private ObjectMapper objectMapper;

  @Autowired private PackagingRepository packagingRepository;

  @BeforeEach
  void setup() {
    packagingRepository.deleteAll();
  }

  @Test
  void testAddAndGetPackaging() throws Exception {
    Packaging box = new Packaging();
    box.setPackagingMaterials("Cardboard Box");
    box.setAverageCost(new BigDecimal("8.00"));
    box.setQuantity(10);

    mockMvc
        .perform(
            post("/packaging/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(box)))
        .andExpect(status().isOk())
        .andExpect(content().string("Packaging Saved"));

    mockMvc
        .perform(get("/packaging"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content[0].packagingMaterials").value("Cardboard Box"));
  }

  @Test
  void testDeletePackaging() throws Exception {
    Packaging tape = new Packaging();
    tape.setPackagingMaterials("Packing Tape");
    tape.setAverageCost(new BigDecimal("3.00"));
    tape.setQuantity(5);

    Packaging saved = packagingRepository.save(tape);

    mockMvc
        .perform(delete("/packaging/delete/" + saved.getId()))
        .andExpect(status().isOk())
        .andExpect(content().string("Packaging deleted"));
  }

  @Test
  void testUpdatePackaging() throws Exception {
    Packaging label = new Packaging();
    label.setPackagingMaterials("Shipping Label");
    label.setAverageCost(new BigDecimal("1.00"));
    label.setQuantity(50);

    Packaging saved = packagingRepository.save(label);
    saved.setAverageCost(new BigDecimal("1.50"));

    mockMvc
        .perform(
            patch("/packaging/update/" + saved.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saved)))
        .andExpect(status().isOk())
        .andExpect(content().string("Packaging updated"));
  }
}
