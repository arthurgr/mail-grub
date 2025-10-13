package com.mailgrub.controller;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.UserPageRequest;
import com.mailgrub.model.Packaging;
import com.mailgrub.service.PackagingService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/{userId}/packaging")
public class PackagingController {

  private final PackagingService service;

  public PackagingController(PackagingService service) {
    this.service = service;
  }

  @GetMapping
  public PagedResponse<Packaging> list(
      @RequestParam(required = false) String name, UserPageRequest req) {
    return PagedResponse.fromPage(
        service.findPage(req.getUserId(), name, req.getPage(), req.getSize()));
  }

  @PostMapping
  public Packaging create(@PathVariable String userId, @RequestBody Packaging body) {
    return service.create(userId, body);
  }

  @PatchMapping("/{id}")
  public Packaging update(
      @PathVariable String userId, @PathVariable Integer id, @RequestBody Packaging patch) {
    return service.update(userId, id, patch);
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable String userId, @PathVariable Integer id) {
    service.delete(userId, id);
  }
}
