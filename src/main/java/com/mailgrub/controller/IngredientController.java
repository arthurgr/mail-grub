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
    public @ResponseBody String addNewIngredient(@RequestBody Ingredient ingredient) {
        ingredientRepository.save(ingredient);
        return "Ingredient Saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Ingredient> getAllIngredients(@RequestParam(required = false) String name) {
        if (name != null && !name.isEmpty()) {
            return ingredientRepository.findByNameContainingIgnoreCase(name);
        }
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
    public @ResponseBody String updateIngredient(@PathVariable Integer id, @RequestBody Ingredient updatedIngredient) {
        return ingredientRepository.findById(id).map(ingredient -> {
            if (updatedIngredient.getName() != null) ingredient.setName(updatedIngredient.getName());
            if (updatedIngredient.getMeasurementType() != null) ingredient.setMeasurementType(updatedIngredient.getMeasurementType());
            if (updatedIngredient.getPurchaseSize() != null) ingredient.setPurchaseSize(updatedIngredient.getPurchaseSize());
            if (updatedIngredient.getAverageCost() != null) ingredient.setAverageCost(updatedIngredient.getAverageCost());
            ingredientRepository.save(ingredient);
            return "Ingredient updated";
        }).orElse("Ingredient not found");
    }
}
