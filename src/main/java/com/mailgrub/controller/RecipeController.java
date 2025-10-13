package com.mailgrub.controller;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.RecipeRequest;
import com.mailgrub.dto.RecipeResponse;
import com.mailgrub.dto.UserPageRequest;
import com.mailgrub.model.Recipe;
import com.mailgrub.service.RecipeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/{userId}/recipes")
public class RecipeController {

  private final RecipeService recipeService;

  public RecipeController(RecipeService recipeService) {
    this.recipeService = recipeService;
  }

  @GetMapping
  public PagedResponse<RecipeResponse> list(
      @RequestParam(required = false) String name, UserPageRequest req) {
    return PagedResponse.fromPage(
        recipeService.findPage(req.getUserId(), name, req.getPage(), req.getSize()));
  }

  @PostMapping
  public Recipe create(@PathVariable String userId, @RequestBody RecipeRequest body) {
    return recipeService.add(userId, body);
  }

  @PatchMapping("/{id}")
  public Recipe update(
      @PathVariable String userId, @PathVariable Integer id, @RequestBody RecipeRequest patch) {
    return recipeService.update(userId, id, patch);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable String userId, @PathVariable Integer id) {
    recipeService.deleteById(userId, id);
  }
}
