package com.mailgrub.repository;

import com.mailgrub.model.Recipe;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface  RecipeRepository extends
        ListCrudRepository<Recipe, Integer>,
        PagingAndSortingRepository<Recipe, Integer> {
}