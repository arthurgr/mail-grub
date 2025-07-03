package com.mailgrub.controller;

import com.mailgrub.dto.PagedResponse;
import com.mailgrub.dto.PagedResponse.Meta;
import com.mailgrub.model.Packaging;
import com.mailgrub.repository.PackagingRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Tag(name = "Packaging", description = "Manage packaging materials")
@RestController
@RequestMapping(path = "/packaging")
public class PackagingController {

    @Autowired
    private PackagingRepository packagingRepository;

    @Operation(summary = "Get paginated list of packaging materials")
    @GetMapping
    public @ResponseBody PagedResponse<Packaging> getPackaging(
            @RequestParam(required = false) String packagingMaterials,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Packaging> pageResult =
                (packagingMaterials != null && !packagingMaterials.trim().isEmpty()) ?
                        packagingRepository.findByPackagingMaterialsContainingIgnoreCase(packagingMaterials.trim(), pageable)
                        : packagingRepository.findAll(pageable);

        Meta meta = new Meta(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.isLast()
        );

        return new PagedResponse<>(pageResult.getContent(), meta);
    }

    @PostMapping(path = "/add")
    public @ResponseBody String addNewPackaging(@RequestBody Packaging packaging) {
        if (packaging.getAverageCost() != null && packaging.getQuantity() != null && packaging.getQuantity() != 0) {
            BigDecimal costPerUnit = packaging.getAverageCost()
                    .divide(BigDecimal.valueOf(packaging.getQuantity()), 4, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);
            packaging.setCostPerUnit(costPerUnit);
        }
        packagingRepository.save(packaging);
        return "Packaging Saved";
    }

    @Operation(summary = "Delete a packaging material by ID")
    @DeleteMapping(path = "/delete/{id}")
    public @ResponseBody String deletePackaging(@PathVariable Integer id) {
        if (!packagingRepository.existsById(id)) {
            return "Packaging not found";
        }
        packagingRepository.deleteById(id);
        return "Packaging deleted";
    }

    @Operation(summary = "Update a packaging material by ID")
    @PatchMapping(path = "/update/{id}")
    public @ResponseBody String updatePackaging(@PathVariable Integer id, @RequestBody Packaging updatedPackaging) {
        return packagingRepository.findById(id).map(packaging -> {
            if (updatedPackaging.getPackagingMaterials() != null) packaging.setPackagingMaterials(updatedPackaging.getPackagingMaterials());
            if (updatedPackaging.getAverageCost() != null) packaging.setAverageCost(updatedPackaging.getAverageCost());
            if (updatedPackaging.getQuantity() != null) packaging.setQuantity(updatedPackaging.getQuantity());
            if (updatedPackaging.getProcurement() != null) packaging.setProcurement(updatedPackaging.getProcurement());
            packagingRepository.save(packaging);
            return "Packaging updated";
        }).orElse("Packaging not found");
    }
}
