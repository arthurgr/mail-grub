package com.mailgrub.impl;
import com.mailgrub.model.Packaging;
import com.mailgrub.repository.PackagingRepository;
import com.mailgrub.service.PackagingService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Service
public class PackagingServiceImpl implements PackagingService {

    private final PackagingRepository repo;

    public PackagingServiceImpl(PackagingRepository repo) {
        this.repo = repo;
    }

    @Override
    @Cacheable(cacheNames = "packagingSearch",
            key = "T(java.util.Objects).hash(#packagingMaterials, #pageable.pageNumber, #pageable.pageSize)")
    public Page<Packaging> find(String packagingMaterials, Pageable pageable) {
        return repo.findByPackagingMaterialsContainingIgnoreCase(packagingMaterials, pageable);
    }

    @Override
    @Cacheable(cacheNames = "packagingSearch",
            key = "T(java.util.Objects).hash('ALL', #pageable.pageNumber, #pageable.pageSize)")
    public Page<Packaging> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    @Cacheable(cacheNames = "packagingById", key = "#id")
    public Packaging getById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @CachePut(cacheNames = "packagingById", key = "#result.id", unless = "#result == null")
    @CacheEvict(cacheNames = "packagingSearch", allEntries = true)
    public Packaging save(Packaging packaging) {
        applyCostPerUnit(packaging);
        return repo.save(packaging);
    }

    @Override
    @CacheEvict(cacheNames = { "packagingById", "packagingSearch" }, allEntries = true)
    public void deleteById(Integer id) {
        repo.deleteById(id);
    }

    private void applyCostPerUnit(Packaging p) {
        Integer qty = p.getQuantity();
        BigDecimal avg = p.getAverageCost();
        if (qty != null && qty != 0 && avg != null) {
            BigDecimal cpu = avg
                    .divide(BigDecimal.valueOf(qty), 4, RoundingMode.HALF_UP)
                    .setScale(2, RoundingMode.HALF_UP);
            p.setCostPerUnit(cpu);
        } else {
            p.setCostPerUnit(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
        }
    }
}