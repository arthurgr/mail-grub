package com.mailgrub.service.impl;

import com.mailgrub.dto.RecipeRequest;
import com.mailgrub.dto.RecipeResponse;
import com.mailgrub.model.Ingredient;
import com.mailgrub.model.Recipe;
import com.mailgrub.model.RecipeIngredient;
import com.mailgrub.repository.IngredientRepository;
import com.mailgrub.repository.RecipeRepository;
import com.mailgrub.service.RecipeService;
import java.util.List;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {

  private final RecipeRepository recipeRepository;
  private final IngredientRepository ingredientRepository;

  public RecipeServiceImpl(
      RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
    this.recipeRepository = recipeRepository;
    this.ingredientRepository = ingredientRepository;
  }

  @Override
  @Cacheable(
      cacheNames = "recipes",
      key = "#tenantId + ':' + (#name == null ? '' : #name) + ':' + #page + ':' + #size")
  public Page<RecipeResponse> findPage(String tenantId, String name, int page, int size) {
    var pageable = PageRequest.of(page, size);
    Page<Recipe> src =
        (name == null || name.isBlank())
            ? recipeRepository.findByTenantId(tenantId, pageable)
            : recipeRepository.findByTenantIdAndNameContainingIgnoreCase(tenantId, name, pageable);
    return src.map(this::toResponse);
  }

  @Override
  @Cacheable(cacheNames = "recipeById", key = "#tenantId + ':' + #id")
  public RecipeResponse getById(String tenantId, Integer id) {
    return recipeRepository.findByIdAndTenantId(id, tenantId).map(this::toResponse).orElse(null);
  }

  @Override
  @CacheEvict(
      cacheNames = {"recipes", "recipeById"},
      allEntries = true)
  public Recipe add(String tenantId, RecipeRequest request) {
    Recipe recipe = new Recipe();
    recipe.setTenantId(tenantId);
    recipe.setName(request.getName() == null ? null : request.getName().trim());
    recipe.setItemsMade(request.getItemsMade());

    List<RecipeIngredient> ris =
        request.getIngredients().stream()
            .map(
                e -> {
                  // Enforce ingredient belongs to the same tenant
                  Ingredient ing =
                      ingredientRepository
                          .findByIdAndTenantId(e.getIngredientId(), tenantId)
                          .orElseThrow(
                              () ->
                                  new IllegalArgumentException(
                                      "Ingredient not found or wrong tenant"));
                  RecipeIngredient ri = new RecipeIngredient();
                  ri.setRecipe(recipe);
                  ri.setIngredient(ing);
                  ri.setAmount(e.getAmount());
                  ri.setOverrideMeasurementType(e.getOverrideMeasurementType());
                  return ri;
                })
            .toList();

    recipe.setRecipeIngredients(ris);
    return recipeRepository.save(recipe);
  }

  @Override
  @CacheEvict(
      cacheNames = {"recipes", "recipeById"},
      allEntries = true)
  public Recipe update(String tenantId, Integer id, RecipeRequest request) {
    return recipeRepository
        .findByIdAndTenantId(id, tenantId)
        .map(
            r -> {
              if (request.getName() != null && !request.getName().trim().isEmpty())
                r.setName(request.getName().trim());
              if (request.getItemsMade() != null) r.setItemsMade(request.getItemsMade());
              if (request.getIngredients() != null && !request.getIngredients().isEmpty()) {
                r.getRecipeIngredients().clear();
                List<RecipeIngredient> updated =
                    request.getIngredients().stream()
                        .map(
                            e -> {
                              Ingredient ing =
                                  ingredientRepository
                                      .findByIdAndTenantId(e.getIngredientId(), tenantId)
                                      .orElseThrow(
                                          () ->
                                              new IllegalArgumentException(
                                                  "Ingredient not found or wrong tenant"));
                              RecipeIngredient ri = new RecipeIngredient();
                              ri.setRecipe(r);
                              ri.setIngredient(ing);
                              ri.setAmount(e.getAmount());
                              ri.setOverrideMeasurementType(e.getOverrideMeasurementType());
                              return ri;
                            })
                        .toList();
                r.getRecipeIngredients().addAll(updated);
              }
              return recipeRepository.save(r);
            })
        .orElse(null);
  }

  @Override
  @CacheEvict(
      cacheNames = {"recipes", "recipeById"},
      allEntries = true)
  public void deleteById(String tenantId, Integer id) {
    // Only delete if this recipe belongs to tenant
    recipeRepository
        .findByIdAndTenantId(id, tenantId)
        .ifPresent(r -> recipeRepository.deleteById(id));
  }

  private RecipeResponse toResponse(Recipe recipe) {
    List<RecipeResponse.IngredientEntry> entries =
        recipe.getRecipeIngredients().stream()
            .map(
                ri ->
                    new RecipeResponse.IngredientEntry(
                        ri.getIngredient().getId(),
                        ri.getIngredient().getName(),
                        ri.getIngredient().getMeasurementType().name(),
                        ri.getIngredient().getPurchaseSize(),
                        ri.getIngredient().getAverageCost().doubleValue(),
                        ri.getAmount(),
                        ri.getOverrideMeasurementType()))
            .toList();

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
