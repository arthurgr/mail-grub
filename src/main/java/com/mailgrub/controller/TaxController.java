package com.mailgrub.controller;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.TenantPageRequest;
import com.mailgrub.model.Tax;
import com.mailgrub.service.TaxService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenants/{tenantId}/taxes")
public class TaxController {

  private final TaxService taxService;

  public TaxController(TaxService taxService) {
    this.taxService = taxService;
  }

  @GetMapping
  public PagedResponse<Tax> list(TenantPageRequest req) {
    return PagedResponse.fromPage(
        taxService.findPage(
            req.getTenantId(), req.getJurisdiction(), req.getPage(), req.getSize()));
  }

  @PostMapping
  public Tax create(@PathVariable String tenantId, @RequestBody Tax body) {
    return taxService.create(tenantId, body);
  }

  @PatchMapping("/{id}")
  public Tax update(
      @PathVariable String tenantId, @PathVariable Integer id, @RequestBody Tax patch) {
    return taxService.update(tenantId, id, patch);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable String tenantId, @PathVariable Integer id) {
    taxService.delete(tenantId, id);
  }
}
