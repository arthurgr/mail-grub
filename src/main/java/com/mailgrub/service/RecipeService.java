package com.mailgrub.service;

import com.mailgrub.dto.RecipeRequest;
import com.mailgrub.dto.RecipeResponse;
import com.mailgrub.model.Recipe;
import org.springframework.data.domain.Page;

public interface RecipeService {
  Page<RecipeResponse> findPage(String userId, String name, int page, int size);

  RecipeResponse getById(String userId, Integer id);

  Recipe add(String userId, RecipeRequest request);

  Recipe update(String userId, Integer id, RecipeRequest request);

  void deleteById(String userId, Integer id);
}
