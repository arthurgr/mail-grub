package com.mailgrub.repository;

import com.mailgrub.model.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface RecipeRepository
        extends ListCrudRepository<Recipe, Integer>, PagingAndSortingRepository<Recipe, Integer> {

    @EntityGraph(attributePaths = { "recipeIngredients", "recipeIngredients.ingredient" })
    Page<Recipe> findByTenantId(String tenantId, Pageable pageable);

    @EntityGraph(attributePaths = { "recipeIngredients", "recipeIngredients.ingredient" })
    Page<Recipe> findByTenantIdAndNameContainingIgnoreCase(String tenantId, String name, Pageable pageable);

    @EntityGraph(attributePaths = { "recipeIngredients", "recipeIngredients.ingredient" })
    Optional<Recipe> findByIdAndTenantId(Integer id, String tenantId);
}
