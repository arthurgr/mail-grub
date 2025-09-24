package com.mailgrub.service;

import com.mailgrub.model.Ingredient;
import org.springframework.data.domain.Page;

public interface IngredientService {
    Page<Ingredient> findPage(String tenantId, String name, int page, int size);
    Ingredient create(String tenantId, Ingredient in);
    Ingredient update(String tenantId, Integer id, Ingredient patch);
    void delete(String tenantId, Integer id);
}