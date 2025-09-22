package com.mailgrub.controller;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.PagedResponse.Meta;
import com.mailgrub.dto.RecipeRequest;
import com.mailgrub.dto.RecipeResponse;
import com.mailgrub.model.Recipe;
import com.mailgrub.service.RecipeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Recipes", description = "Endpoints for managing recipes")
@RestController
@RequestMapping(path = "/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @Operation(summary = "Get paginated list of recipes with ingredients and costs")
    @GetMapping
    public @ResponseBody PagedResponse<RecipeResponse> getAllRecipes(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeResponse> pageResult = recipeService.findAll(pageable);

        Meta meta =
                new Meta(
                        pageResult.getNumber(),
                        pageResult.getSize(),
                        pageResult.getTotalElements(),
                        pageResult.getTotalPages(),
                        pageResult.isLast());

        return new PagedResponse<>(pageResult.getContent(), meta);
    }

    @Operation(summary = "Create a new recipe with ingredients and item count")
    @PostMapping("/add")
    public @ResponseBody String addRecipe(@RequestBody RecipeRequest request) {
        recipeService.add(request);
        return "Recipe saved";
    }

    @Operation(summary = "Update a recipe by ID")
    @PatchMapping("/update/{id}")
    public @ResponseBody String updateRecipe(
            @PathVariable Integer id, @RequestBody RecipeRequest updateRequest) {
        Recipe r = recipeService.update(id, updateRequest);
        return r == null ? "Recipe not found" : "Recipe updated";
    }

    @Operation(summary = "Delete a recipe by ID")
    @DeleteMapping("/delete/{id}")
    public @ResponseBody String deleteRecipe(@PathVariable Integer id) {
        recipeService.deleteById(id);
        return "Recipe deleted";
    }
}
