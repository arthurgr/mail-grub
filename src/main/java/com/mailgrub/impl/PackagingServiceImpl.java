package com.mailgrub.impl;

import com.mailgrub.model.Packaging;
import com.mailgrub.repository.PackagingRepository;
import com.mailgrub.service.PackagingService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
@Transactional
public class PackagingServiceImpl implements PackagingService {

    private final PackagingRepository repo;

    public PackagingServiceImpl(PackagingRepository repo) {
        this.repo = repo;
    }

    @Override
    @Cacheable(
            cacheNames = "packaging",
            key = "#tenantId + ':' + (#packagingMaterials == null ? '' : #packagingMaterials) + ':' + #page + ':' + #size"
    )
    public Page<Packaging> findPage(String tenantId, String packagingMaterials, int page, int size) {
        var pageable = PageRequest.of(page, size);
        if (packagingMaterials == null || packagingMaterials.isBlank()) {
            return repo.findByTenantId(tenantId, pageable);
        }
        return repo.findByTenantIdAndPackagingMaterialsContainingIgnoreCase(tenantId, packagingMaterials, pageable);
    }

    @Override
    @CacheEvict(cacheNames = "packaging", key = "#tenantId + ':*'", allEntries = true)
    public Packaging create(String tenantId, Packaging in) {
        in.setId(null);
        in.setTenantId(tenantId);
        applyCostPerUnit(in);
        return repo.save(in);
    }

    @Override
    @CacheEvict(cacheNames = "packaging", key = "#tenantId + ':*'", allEntries = true)
    public Packaging update(String tenantId, Integer id, Packaging patch) {
        var existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
        if (!tenantId.equals(existing.getTenantId())) {
            throw new IllegalArgumentException("Wrong tenant for entity");
        }
        if (patch.getPackagingMaterials() != null) existing.setPackagingMaterials(patch.getPackagingMaterials());
        if (patch.getAverageCost() != null) existing.setAverageCost(patch.getAverageCost());
        if (patch.getQuantity() != null) existing.setQuantity(patch.getQuantity());
        if (patch.getProcurement() != null) existing.setProcurement(patch.getProcurement());
        applyCostPerUnit(existing);
        return repo.save(existing);
    }

    @Override
    @CacheEvict(cacheNames = "packaging", key = "#tenantId + ':*'", allEntries = true)
    public void delete(String tenantId, Integer id) {
        var existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
        if (!tenantId.equals(existing.getTenantId())) {
            throw new IllegalArgumentException("Wrong tenant for entity");
        }
        repo.deleteById(id);
    }

    private void applyCostPerUnit(Packaging p) {
        Integer qty = p.getQuantity();
        BigDecimal avg = p.getAverageCost();
        if (qty != null && qty > 0 && avg != null) {
            p.setCostPerUnit(
                    avg.divide(BigDecimal.valueOf(qty), 4, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP)
            );
        } else {
            p.setCostPerUnit(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }
    }
}
