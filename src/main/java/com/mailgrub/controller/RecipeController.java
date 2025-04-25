package com.mailgrub.controller;

import com.mailgrub.dto.RecipeRequest;
import com.mailgrub.dto.RecipeUpdateRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mailgrub.model.Recipe;
import com.mailgrub.model.Ingredient;
import com.mailgrub.repository.IngredientRepository;
import com.mailgrub.repository.RecipeRepository;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.PagedResponse.Meta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RestController
@RequestMapping(path = "/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping
    public @ResponseBody PagedResponse<Recipe> getAllRecipes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Recipe> pageResult = recipeRepository.findAll(pageable);

        Meta meta = new Meta(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
        );

        return new PagedResponse<>(pageResult.getContent(), meta);
    }

    @PostMapping("/add")
    public @ResponseBody String addRecipe(@RequestBody RecipeRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return "Recipe name is required";
        }

        Recipe recipe = new Recipe();
        recipe.setName(request.getName().trim());

        if (request.getIngredientIds() != null && !request.getIngredientIds().isEmpty()) {
            List<Ingredient> ingredients = (List<Ingredient>) ingredientRepository.findAllById(request.getIngredientIds());
            recipe.setIngredients(ingredients);
        }

        recipeRepository.save(recipe);
        return "Recipe saved";
    }


    @PatchMapping("/update/{id}")
    public @ResponseBody String updateRecipe(
            @PathVariable Integer id,
            @RequestBody RecipeUpdateRequest updateRequest
    ) {
        return recipeRepository.findById(id).map(recipe -> {
            if (updateRequest.getName() != null && !updateRequest.getName().trim().isEmpty()) {
                recipe.setName(updateRequest.getName().trim());
            }

            if (updateRequest.getIngredientIds() != null && !updateRequest.getIngredientIds().isEmpty()) {
                List<Ingredient> ingredients = (List<Ingredient>) ingredientRepository.findAllById(updateRequest.getIngredientIds());
                recipe.setIngredients(ingredients);
            }

            recipeRepository.save(recipe);
            return "Recipe updated";
        }).orElse("Recipe not found");
    }

    @DeleteMapping("/delete/{id}")
    public @ResponseBody String deleteRecipe(@PathVariable Integer id) {
        if (!recipeRepository.existsById(id)) {
            return "Recipe not found";
        }
        recipeRepository.deleteById(id);
        return "Recipe deleted";
    }

}
