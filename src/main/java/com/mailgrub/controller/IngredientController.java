package com.mailgrub.controller;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.PagedResponse.Meta;
import com.mailgrub.model.Ingredient;
import com.mailgrub.repository.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/ingredients")
public class IngredientController {

    @Autowired
    private IngredientRepository ingredientRepository;

    @GetMapping
    public @ResponseBody PagedResponse<Ingredient> getIngredients(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Ingredient> pageResult =
                (name != null && !name.trim().isEmpty()) ?
                        ingredientRepository.findByNameContainingIgnoreCase(name.trim(), pageable)
                        : ingredientRepository.findAll(pageable);

        Meta meta = new Meta(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
        );

        return new PagedResponse<>(pageResult.getContent(), meta);
    }

    @PostMapping(path = "/add")
    public @ResponseBody String addNewIngredient(@RequestBody Ingredient ingredient) {
        ingredientRepository.save(ingredient);
        return "Ingredient Saved";
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
