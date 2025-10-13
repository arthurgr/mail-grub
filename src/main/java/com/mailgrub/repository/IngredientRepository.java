package com.mailgrub.repository;

import com.mailgrub.model.Ingredient;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface IngredientRepository
    extends ListCrudRepository<Ingredient, Integer>,
        PagingAndSortingRepository<Ingredient, Integer> {

  Page<Ingredient> findByUserIdAndNameContainingIgnoreCase(
      String userId, String name, Pageable pageable);

  Page<Ingredient> findByUserId(String userId, Pageable pageable);

  Optional<Ingredient> findByIdAndUserId(Integer id, String userId);
}
