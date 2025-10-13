package com.mailgrub.controller;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.UserPageRequest;
import com.mailgrub.model.Ingredient;
import com.mailgrub.service.IngredientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/{userId}/ingredients")
public class IngredientController {

  private final IngredientService service;

  public IngredientController(IngredientService service) {
    this.service = service;
  }

  @GetMapping
  public PagedResponse<Ingredient> list(
      @RequestParam(required = false) String name, UserPageRequest req) {
    return PagedResponse.fromPage(
        service.findPage(req.getUserId(), name, req.getPage(), req.getSize()));
  }

  @PostMapping
  public Ingredient create(@PathVariable String userId, @RequestBody Ingredient body) {
    return service.create(userId, body);
  }

  @PatchMapping("/{id}")
  public Ingredient update(
      @PathVariable String userId, @PathVariable Integer id, @RequestBody Ingredient patch) {
    return service.update(userId, id, patch);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable String userId, @PathVariable Integer id) {
    service.delete(userId, id);
  }
}
