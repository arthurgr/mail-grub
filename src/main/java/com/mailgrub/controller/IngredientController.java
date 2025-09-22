package com.mailgrub.controller;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.PagedResponse.Meta;
import com.mailgrub.model.Ingredient;
import com.mailgrub.service.IngredientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Ingredients", description = "Manage recipe ingredients")
@RestController
@RequestMapping(path = "/ingredients")
public class IngredientController {

  private final IngredientService ingredientService;

  public IngredientController(IngredientService ingredientService) {
    this.ingredientService = ingredientService;
  }

  @Operation(summary = "Get paginated list of ingredients")
  @GetMapping
  public @ResponseBody PagedResponse<Ingredient> getIngredients(
          @RequestParam(required = false) String name,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Ingredient> pageResult =
            (name != null && !name.trim().isEmpty())
                    ? ingredientService.find(name.trim(), pageable)
                    : ingredientService.findAll(pageable);

    Meta meta =
            new Meta(
                    pageResult.getNumber(),
                    pageResult.getSize(),
                    pageResult.getTotalElements(),
                    pageResult.getTotalPages(),
                    pageResult.isLast());

    return new PagedResponse<>(pageResult.getContent(), meta);
  }

  @Operation(summary = "Add a new ingredient")
  @PostMapping(path = "/add")
  public @ResponseBody String addNewIngredient(@RequestBody Ingredient ingredient) {
    ingredientService.save(ingredient);
    return "Ingredient Saved";
  }

  @Operation(summary = "Delete an ingredient by ID")
  @DeleteMapping(path = "/delete/{id}")
  public @ResponseBody String deleteIngredient(@PathVariable Integer id) {
    if (ingredientService.getById(id) == null) return "Ingredient not found";
    ingredientService.deleteById(id);
    return "Ingredient deleted";
  }

  @Operation(summary = "Update an ingredient by ID")
  @PatchMapping(path = "/update/{id}")
  public @ResponseBody String updateIngredient(
          @PathVariable Integer id, @RequestBody Ingredient updated) {
    Ingredient existing = ingredientService.getById(id);
    if (existing == null) return "Ingredient not found";
    if (updated.getName() != null) existing.setName(updated.getName());
    if (updated.getMeasurementType() != null) existing.setMeasurementType(updated.getMeasurementType());
    if (updated.getPurchaseSize() != null) existing.setPurchaseSize(updated.getPurchaseSize());
    if (updated.getAverageCost() != null) existing.setAverageCost(updated.getAverageCost());
    ingredientService.save(existing);
    return "Ingredient updated";
  }
}
