package com.mailgrub.controller;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.PagedResponse.Meta;
import com.mailgrub.model.Tax;
import com.mailgrub.service.TaxService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Taxes", description = "Manage tax jurisdictions")
@RestController
@RequestMapping(path = "/taxes")
public class TaxController {

  private final TaxService taxService;

  public TaxController(TaxService taxService) {
    this.taxService = taxService;
  }

  @Operation(summary = "Get paginated list of taxes")
  @GetMapping
  public @ResponseBody PagedResponse<Tax> getTaxes(
          @RequestParam(required = false) String jurisdiction,
          @RequestParam(defaultValue = "0") int page,
          @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    Page<Tax> pageResult =
            (jurisdiction != null && !jurisdiction.trim().isEmpty())
                    ? taxService.find(jurisdiction.trim(), pageable)
                    : taxService.findAll(pageable);

    Meta meta =
            new Meta(
                    pageResult.getNumber(),
                    pageResult.getSize(),
                    pageResult.getTotalElements(),
                    pageResult.getTotalPages(),
                    pageResult.isLast());

    return new PagedResponse<>(pageResult.getContent(), meta);
  }

  @Operation(summary = "Add a new tax record")
  @PostMapping(path = "/add")
  public @ResponseBody String addNewTax(@RequestBody Tax tax) {
    taxService.save(tax);
    return "Tax record saved";
  }

  @Operation(summary = "Delete a tax record by ID")
  @DeleteMapping(path = "/delete/{id}")
  public @ResponseBody String deleteTax(@PathVariable Integer id) {
    if (taxService.getById(id) == null) {
      return "Tax record not found";
    }
    taxService.deleteById(id);
    return "Tax record deleted";
  }

  @Operation(summary = "Update a tax record by ID")
  @PatchMapping(path = "/update/{id}")
  public @ResponseBody String updateTax(@PathVariable Integer id, @RequestBody Tax updatedTax) {
    Tax existing = taxService.getById(id);
    if (existing == null) return "Tax record not found";
    if (updatedTax.getJurisdiction() != null) existing.setJurisdiction(updatedTax.getJurisdiction());
    if (updatedTax.getTaxRate() != null) existing.setTaxRate(updatedTax.getTaxRate());
    taxService.save(existing);
    return "Tax record updated";
  }
}
