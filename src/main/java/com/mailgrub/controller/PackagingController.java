package com.mailgrub.controller;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.TenantPageRequest;
import com.mailgrub.model.Packaging;
import com.mailgrub.service.PackagingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenants/{tenantId}/packaging")
public class PackagingController {

  private final PackagingService service;

  public PackagingController(PackagingService service) {
    this.service = service;
  }

  @GetMapping
  public PagedResponse<Packaging> list(
      @RequestParam(required = false) String name, TenantPageRequest req) {
    return PagedResponse.fromPage(
        service.findPage(req.getTenantId(), name, req.getPage(), req.getSize()));
  }

  @PostMapping
  public Packaging create(@PathVariable String tenantId, @RequestBody Packaging body) {
    return service.create(tenantId, body);
  }

  @PatchMapping("/{id}")
  public Packaging update(
      @PathVariable String tenantId, @PathVariable Integer id, @RequestBody Packaging patch) {
    return service.update(tenantId, id, patch);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable String tenantId, @PathVariable Integer id) {
    service.delete(tenantId, id);
  }
}
