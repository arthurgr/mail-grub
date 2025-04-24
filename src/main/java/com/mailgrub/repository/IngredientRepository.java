package com.mailgrub.repository;

import org.springframework.data.repository.CrudRepository;

import com.mailgrub.model.Ingredient;

import java.util.List;

public interface IngredientRepository extends CrudRepository<Ingredient, Integer> {
    List<Ingredient> findByNameContainingIgnoreCase(String name);
}
