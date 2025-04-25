package com.mailgrub.controller;

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
    public @ResponseBody String addRecipe(@RequestBody Recipe recipe) {
        recipeRepository.save(recipe);
        return "Recipe saved";
    }

    @DeleteMapping("/delete/{id}")
    public @ResponseBody String deleteRecipe(@PathVariable Integer id) {
        if (!recipeRepository.existsById(id)) {
            return "Recipe not found";
        }
        recipeRepository.deleteById(id);
        return "Recipe deleted";
    }

    @PostMapping("/add-with-ingredient-ids")
    public @ResponseBody String addRecipeWithIngredientIds(
            @RequestParam String name,
            @RequestBody List<Integer> ingredientIds
    ) {
        List<Ingredient> ingredients = (List<Ingredient>) ingredientRepository.findAllById(ingredientIds);
        Recipe recipe = new Recipe();
        recipe.setName(name);
        recipe.setIngredients(ingredients);
        recipeRepository.save(recipe);
        return "Recipe with ingredients saved";
    }

}
