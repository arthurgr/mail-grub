package com.mailgrub.controller;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.RecipeRequest;
import com.mailgrub.dto.RecipeResponse;
import com.mailgrub.dto.TenantPageRequest;
import com.mailgrub.model.Recipe;
import com.mailgrub.service.RecipeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenants/{tenantId}/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping
    public PagedResponse<RecipeResponse> list(TenantPageRequest req) {
        return PagedResponse.fromPage(
                recipeService.findPage(req.getTenantId(), req.getName(), req.getPage(), req.getSize())
        );
    }

    @PostMapping
    public Recipe create(@PathVariable String tenantId, @RequestBody RecipeRequest body) {
        return recipeService.add(tenantId, body);
    }

    @PatchMapping("/{id}")
    public Recipe update(@PathVariable String tenantId, @PathVariable Integer id, @RequestBody RecipeRequest patch) {
        return recipeService.update(tenantId, id, patch);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable String tenantId, @PathVariable Integer id) {
        recipeService.deleteById(tenantId, id);
    }
}
