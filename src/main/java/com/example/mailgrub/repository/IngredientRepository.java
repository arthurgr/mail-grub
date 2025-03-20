package com.example.mailgrub.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.mailgrub.model.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, Integer> {
    
}
