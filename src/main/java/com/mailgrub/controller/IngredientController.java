package com.mailgrub.controller;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.TenantPageRequest;
import com.mailgrub.model.Ingredient;
import com.mailgrub.service.IngredientService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenants/{tenantId}/ingredients")
public class IngredientController {

  private final IngredientService service;

  public IngredientController(IngredientService service) {
    this.service = service;
  }

  @GetMapping
  public PagedResponse<Ingredient> list(
      @RequestParam(required = false) String name, TenantPageRequest req) {
    return PagedResponse.fromPage(
        service.findPage(req.getTenantId(), name, req.getPage(), req.getSize()));
  }

  @PostMapping
  public Ingredient create(@PathVariable String tenantId, @RequestBody Ingredient body) {
    return service.create(tenantId, body);
  }

  @PatchMapping("/{id}")
  public Ingredient update(
      @PathVariable String tenantId, @PathVariable Integer id, @RequestBody Ingredient patch) {
    return service.update(tenantId, id, patch);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable String tenantId, @PathVariable Integer id) {
    service.delete(tenantId, id);
  }
}
