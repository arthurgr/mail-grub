package com.mailgrub.impl;
import com.mailgrub.dto.RecipeRequest;
import com.mailgrub.dto.RecipeResponse;
import com.mailgrub.model.Ingredient;
import com.mailgrub.model.Recipe;
import com.mailgrub.model.RecipeIngredient;
import com.mailgrub.repository.IngredientRepository;
import com.mailgrub.repository.RecipeRepository;
import com.mailgrub.service.RecipeService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public RecipeServiceImpl(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    @Cacheable(cacheNames = "recipesSearch",
            key = "T(java.util.Objects).hash('ALL', #pageable.pageNumber, #pageable.pageSize)")
    public Page<RecipeResponse> findAll(Pageable pageable) {
        return recipeRepository.findAll(pageable).map(this::toResponse);
    }

    @Override
    @Cacheable(cacheNames = "recipesById", key = "#id")
    public RecipeResponse getById(Integer id) {
        return recipeRepository.findById(id).map(this::toResponse).orElse(null);
    }

    @Override
    @CacheEvict(cacheNames = { "recipesById", "recipesSearch" }, allEntries = true)
    public Recipe add(RecipeRequest request) {
        Recipe recipe = new Recipe();
        recipe.setName(request.getName() == null ? null : request.getName().trim());
        recipe.setItemsMade(request.getItemsMade());
        List<RecipeIngredient> ris =
                request.getIngredients().stream().map(e -> {
                    Ingredient ing = ingredientRepository.findById(e.getIngredientId()).orElseThrow();
                    RecipeIngredient ri = new RecipeIngredient();
                    ri.setRecipe(recipe);
                    ri.setIngredient(ing);
                    ri.setAmount(e.getAmount());
                    ri.setOverrideMeasurementType(e.getOverrideMeasurementType());
                    return ri;
                }).toList();
        recipe.setRecipeIngredients(ris);
        return recipeRepository.save(recipe);
    }

    @Override
    @CacheEvict(cacheNames = { "recipesById", "recipesSearch" }, allEntries = true)
    public Recipe update(Integer id, RecipeRequest request) {
        return recipeRepository.findById(id).map(r -> {
            if (request.getName() != null && !request.getName().trim().isEmpty()) r.setName(request.getName().trim());
            if (request.getItemsMade() != null) r.setItemsMade(request.getItemsMade());
            if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
                r.getRecipeIngredients().clear();
                List<RecipeIngredient> updated = request.getIngredients().stream().map(e -> {
                    Ingredient ing = ingredientRepository.findById(e.getIngredientId()).orElseThrow();
                    RecipeIngredient ri = new RecipeIngredient();
                    ri.setRecipe(r);
                    ri.setIngredient(ing);
                    ri.setAmount(e.getAmount());
                    ri.setOverrideMeasurementType(e.getOverrideMeasurementType());
                    return ri;
                }).toList();
                r.getRecipeIngredients().addAll(updated);
            }
            return recipeRepository.save(r);
        }).orElse(null);
    }

    @Override
    @CacheEvict(cacheNames = { "recipesById", "recipesSearch" }, allEntries = true)
    public void deleteById(Integer id) {
        recipeRepository.deleteById(id);
    }

    private RecipeResponse toResponse(Recipe recipe) {
        List<RecipeResponse.IngredientEntry> entries = recipe.getRecipeIngredients().stream().map(ri ->
                new RecipeResponse.IngredientEntry(
                        ri.getIngredient().getId(),
                        ri.getIngredient().getName(),
                        ri.getIngredient().getMeasurementType().name(),
                        ri.getIngredient().getPurchaseSize(),
                        ri.getIngredient().getAverageCost().doubleValue(),
                        ri.getAmount(),
                        ri.getOverrideMeasurementType()
                )
        ).toList();

        RecipeResponse resp = new RecipeResponse();
        resp.setId(recipe.getId());
        resp.setName(recipe.getName());
        resp.setItemsMade(recipe.getItemsMade());
        resp.setIngredients(entries);
        resp.setTotalCost(recipe.getTotalCost());
        resp.setCostPerItem(recipe.getCostPerItem());
        return resp;
    }
}
