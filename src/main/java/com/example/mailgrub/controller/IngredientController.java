package com.example.mailgrub.controller;

import com.example.mailgrub.model.Ingredient;
import com.example.mailgrub.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController // Use @RestController instead of @Controller for API responses
@RequestMapping(path = "/ingredients") // Base path for Ingredient API
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepository;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewIngredient(@RequestParam String name,
                                                 @RequestParam String measurementType,
                                                 @RequestParam Double purchaseSize,
                                                 @RequestParam Double averageCost) {
        Ingredient ingredient = new Ingredient();
        ingredient.setName(name);
        ingredient.setMeasurementType(measurementType);
        ingredient.setPurchaseSize(purchaseSize);
        ingredient.setAverageCost(averageCost);

        ingredientRepository.save(ingredient);
        return "Ingredient Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Ingredient> getAllIngredients() {
        return ingredientRepository.findAll();
    }
}
