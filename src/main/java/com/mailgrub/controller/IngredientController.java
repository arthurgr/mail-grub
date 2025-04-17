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

    @DeleteMapping(path = "/delete/{id}")
    public @ResponseBody String deleteIngredient(@PathVariable Integer id) {
        if (!ingredientRepository.existsById(id)) {
            return "Ingredient not found";
        }
        ingredientRepository.deleteById(id);
        return "Ingredient deleted";
    }

    @PatchMapping(path = "/update/{id}")
    public @ResponseBody String updateIngredient(@PathVariable Integer id,
                                                 @RequestParam(required = false) String name,
                                                 @RequestParam(required = false) String measurementType,
                                                 @RequestParam(required = false) Double purchaseSize,
                                                 @RequestParam(required = false) Double averageCost) {
        return ingredientRepository.findById(id).map(ingredient -> {
            if (name != null) ingredient.setName(name);
            if (measurementType != null) ingredient.setMeasurementType(measurementType);
            if (purchaseSize != null) ingredient.setPurchaseSize(purchaseSize);
            if (averageCost != null) ingredient.setAverageCost(averageCost);
            ingredientRepository.save(ingredient);
            return "Ingredient updated";
        }).orElse("Ingredient not found");
    }
}
