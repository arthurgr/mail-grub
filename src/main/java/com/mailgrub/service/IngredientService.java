package com.mailgrub.service;

import com.mailgrub.model.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IngredientService {
    Page<Ingredient> find(String name, Pageable pageable);
    Page<Ingredient> findAll(Pageable pageable);
    Ingredient getById(Integer id);
    Ingredient save(Ingredient ingredient);
    void deleteById(Integer id);
}
