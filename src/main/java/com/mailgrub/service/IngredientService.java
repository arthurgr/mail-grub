package com.mailgrub.service;

import com.mailgrub.model.Ingredient;
import org.springframework.data.domain.Page;

public interface IngredientService {
  Page<Ingredient> findPage(String userId, String name, int page, int size);

  Ingredient create(String userId, Ingredient in);

  Ingredient update(String userId, Integer id, Ingredient patch);

  void delete(String userId, Integer id);
}
