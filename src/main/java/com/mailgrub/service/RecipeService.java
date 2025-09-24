package com.mailgrub.service;

import com.mailgrub.dto.RecipeRequest;
import com.mailgrub.dto.RecipeResponse;
import com.mailgrub.model.Recipe;
import org.springframework.data.domain.Page;

public interface RecipeService {
    Page<RecipeResponse> findPage(String tenantId, String name, int page, int size);
    RecipeResponse getById(String tenantId, Integer id);
    Recipe add(String tenantId, RecipeRequest request);
    Recipe update(String tenantId, Integer id, RecipeRequest request);
    void deleteById(String tenantId, Integer id);
}
