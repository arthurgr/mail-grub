package com.mailgrub.repository;

import com.mailgrub.model.Ingredient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import java.util.Optional;

public interface IngredientRepository
        extends ListCrudRepository<Ingredient, Integer>,
        PagingAndSortingRepository<Ingredient, Integer> {

  Page<Ingredient> findByTenantIdAndNameContainingIgnoreCase(
          String tenantId, String name, Pageable pageable);

  Page<Ingredient> findByTenantId(String tenantId, Pageable pageable);

  Optional<Ingredient> findByIdAndTenantId(Integer id, String tenantId);
}