package com.mailgrub.controller;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.UserPageRequest;
import com.mailgrub.model.Tax;
import com.mailgrub.service.TaxService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/{userId}/taxes")
public class TaxController {

  private final TaxService taxService;

  public TaxController(TaxService taxService) {
    this.taxService = taxService;
  }

  @GetMapping
  public PagedResponse<Tax> list(
      @RequestParam(required = false) String jurisdiction, UserPageRequest req) {
    return PagedResponse.fromPage(
        taxService.findPage(req.getUserId(), jurisdiction, req.getPage(), req.getSize()));
  }

  @PostMapping
  public Tax create(@PathVariable String userId, @RequestBody Tax body) {
    return taxService.create(userId, body);
  }

  @PatchMapping("/{id}")
  public Tax update(
      @PathVariable String userId, @PathVariable Integer id, @RequestBody Tax patch) {
    return taxService.update(userId, id, patch);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable String userId, @PathVariable Integer id) {
    taxService.delete(userId, id);
  }
}
