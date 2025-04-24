package com.mailgrub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mailgrub.model.Recipe;
import com.mailgrub.model.Ingredient;
import com.mailgrub.repository.IngredientRepository;
import com.mailgrub.repository.RecipeRepository;

import java.util.List;

@RestController
@RequestMapping(path = "/recipes")
public class RecipeController {

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping("/all")
    public @ResponseBody Iterable<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
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
