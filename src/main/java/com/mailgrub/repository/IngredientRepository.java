package com.mailgrub.repository;

import com.mailgrub.model.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IngredientRepository
    extends ListCrudRepository<Ingredient, Integer>,
        PagingAndSortingRepository<Ingredient, Integer> {

  Page<Ingredient> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
