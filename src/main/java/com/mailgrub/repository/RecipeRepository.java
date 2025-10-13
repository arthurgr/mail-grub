package com.mailgrub.repository;

import com.mailgrub.model.Recipe;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecipeRepository
    extends ListCrudRepository<Recipe, Integer>, PagingAndSortingRepository<Recipe, Integer> {

  @EntityGraph(attributePaths = {"recipeIngredients", "recipeIngredients.ingredient"})
  Page<Recipe> findByUserId(String userId, Pageable pageable);

  @EntityGraph(attributePaths = {"recipeIngredients", "recipeIngredients.ingredient"})
  Page<Recipe> findByUserIdAndNameContainingIgnoreCase(
      String userId, String name, Pageable pageable);

  @EntityGraph(attributePaths = {"recipeIngredients", "recipeIngredients.ingredient"})
  Optional<Recipe> findByIdAndUserId(Integer id, String userId);
}
