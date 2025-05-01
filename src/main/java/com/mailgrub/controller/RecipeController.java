package com.mailgrub.controller;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.PagedResponse.Meta;
import com.mailgrub.dto.RecipeRequest;
import com.mailgrub.dto.RecipeResponse;
import com.mailgrub.model.Ingredient;
import com.mailgrub.model.Recipe;
import com.mailgrub.model.RecipeIngredient;
import com.mailgrub.repository.IngredientRepository;
import com.mailgrub.repository.RecipeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Recipes", description = "Endpoints for managing recipes")
@RestController
@RequestMapping(path = "/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Operation(summary = "Get paginated list of recipes with ingredients and costs")
    @GetMapping
    public @ResponseBody PagedResponse<RecipeResponse> getAllRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> pageResult = recipeRepository.findAll(pageable);

        List<RecipeResponse> content = pageResult.getContent().stream().map(recipe -> {
            List<RecipeResponse.IngredientEntry> ingredients = recipe.getRecipeIngredients().stream().map(ri -> {
                Ingredient ing = ri.getIngredient();
                return new RecipeResponse.IngredientEntry(
                        ing.getId(),
                        ing.getName(),
                        ing.getMeasurementType().name(),
                        ing.getPurchaseSize(),
                        ing.getAverageCost().doubleValue(),
                        ri.getAmount()
                );
            }).toList();

            RecipeResponse response = new RecipeResponse();
            response.setId(recipe.getId());
            response.setName(recipe.getName());
            response.setItemsMade(recipe.getItemsMade());
            response.setIngredients(ingredients);
            response.setTotalCost(recipe.getTotalCost());
            response.setCostPerItem(recipe.getCostPerItem());
            return response;
        }).toList();

        Meta meta = new Meta(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
        );

        return new PagedResponse<>(content, meta);
    }

    @Operation(summary = "Create a new recipe with ingredients and item count")
    @PostMapping("/add")
    public @ResponseBody String addRecipe(@RequestBody RecipeRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return "Recipe name is required";
        }

        Recipe recipe = new Recipe();
        recipe.setName(request.getName().trim());
        recipe.setItemsMade(request.getItemsMade());

        List<RecipeIngredient> recipeIngredients = request.getIngredients().stream().map(entry -> {
            Ingredient ingredient = ingredientRepository.findById(entry.getIngredientId()).orElseThrow();
            RecipeIngredient ri = new RecipeIngredient();
            ri.setRecipe(recipe);
            ri.setIngredient(ingredient);
            ri.setAmount(entry.getAmount());
            return ri;
        }).toList();

        recipe.setRecipeIngredients(recipeIngredients);
        recipeRepository.save(recipe);
        return "Recipe saved";
    }

    @Operation(summary = "Update a recipe by ID")
    @PatchMapping("/update/{id}")
    public @ResponseBody String updateRecipe(
            @PathVariable Integer id,
            @RequestBody RecipeRequest updateRequest
    ) {
        return recipeRepository.findById(id).map(recipe -> {
            if (updateRequest.getName() != null && !updateRequest.getName().trim().isEmpty()) {
                recipe.setName(updateRequest.getName().trim());
            }

            if (updateRequest.getItemsMade() != null) {
                recipe.setItemsMade(updateRequest.getItemsMade());
            }

            if (updateRequest.getIngredients() != null && !updateRequest.getIngredients().isEmpty()) {
                recipe.getRecipeIngredients().clear();

                List<RecipeIngredient> updatedIngredients = updateRequest.getIngredients().stream().map(entry -> {
                    Ingredient ingredient = ingredientRepository.findById(entry.getIngredientId()).orElseThrow();
                    RecipeIngredient ri = new RecipeIngredient();
                    ri.setRecipe(recipe);
                    ri.setIngredient(ingredient);
                    ri.setAmount(entry.getAmount());
                    return ri;
                }).toList();

                recipe.getRecipeIngredients().addAll(updatedIngredients);
            }

            recipeRepository.save(recipe);
            return "Recipe updated";
        }).orElse("Recipe not found");
    }

    @Operation(summary = "Delete a recipe by ID")
    @DeleteMapping("/delete/{id}")
    public @ResponseBody String deleteRecipe(@PathVariable Integer id) {
        if (!recipeRepository.existsById(id)) {
            return "Recipe not found";
        }
        recipeRepository.deleteById(id);
        return "Recipe deleted";
    }
}
