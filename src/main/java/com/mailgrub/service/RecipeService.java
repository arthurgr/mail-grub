package com.mailgrub.service;

import com.mailgrub.dto.RecipeRequest;
import com.mailgrub.dto.RecipeResponse;
import com.mailgrub.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecipeService {
    Page<RecipeResponse> findAll(Pageable pageable);
    RecipeResponse getById(Integer id);
    Recipe add(RecipeRequest request);
    Recipe update(Integer id, RecipeRequest request);
    void deleteById(Integer id);
}