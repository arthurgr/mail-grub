package com.mailgrub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.mailgrub.model.Ingredient;
import com.mailgrub.repository.IngredientRepository;

@RestController
@RequestMapping(path = "/ingredients")
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
