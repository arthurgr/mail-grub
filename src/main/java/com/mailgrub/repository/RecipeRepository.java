package com.mailgrub.repository;

import org.springframework.data.repository.CrudRepository;
import com.mailgrub.model.Recipe;

public interface RecipeRepository extends CrudRepository<Recipe, Integer> {
}
