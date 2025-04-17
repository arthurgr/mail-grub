package com.mailgrub.repository;

import org.springframework.data.repository.CrudRepository;

import com.mailgrub.model.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, Integer> {
    
}
