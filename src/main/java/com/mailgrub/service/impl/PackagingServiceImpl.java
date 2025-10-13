package com.mailgrub.service.impl;

import com.mailgrub.model.Packaging;
import com.mailgrub.repository.PackagingRepository;
import com.mailgrub.service.PackagingService;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
      key =
          "#userId + ':' + (#packagingMaterials == null ? '' : #packagingMaterials) + ':' + #page + ':' + #size")
  public Page<Packaging> findPage(String userId, String packagingMaterials, int page, int size) {
    var pageable = PageRequest.of(page, size);
    if (packagingMaterials == null || packagingMaterials.isBlank()) {
      return repo.findByUserId(userId, pageable);
    }
    return repo.findByUserIdAndPackagingMaterialsContainingIgnoreCase(
        userId, packagingMaterials, pageable);
  }

  @Override
  @CacheEvict(cacheNames = "packaging", key = "#userId + ':*'", allEntries = true)
  public Packaging create(String userId, Packaging in) {
    in.setId(null);
    in.setUserId(userId);
    applyCostPerUnit(in);
    return repo.save(in);
  }

  @Override
  @CacheEvict(cacheNames = "packaging", key = "#userId + ':*'", allEntries = true)
  public Packaging update(String userId, Integer id, Packaging patch) {
    var existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    if (!userId.equals(existing.getUserId())) {
      throw new IllegalArgumentException("Wrong user for entity");
    }
    if (patch.getPackagingMaterials() != null)
      existing.setPackagingMaterials(patch.getPackagingMaterials());
    if (patch.getAverageCost() != null) existing.setAverageCost(patch.getAverageCost());
    if (patch.getQuantity() != null) existing.setQuantity(patch.getQuantity());
    if (patch.getProcurement() != null) existing.setProcurement(patch.getProcurement());
    applyCostPerUnit(existing);
    return repo.save(existing);
  }

  @Override
  @CacheEvict(cacheNames = "packaging", key = "#userId + ':*'", allEntries = true)
  public void delete(String userId, Integer id) {
    var existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    if (!userId.equals(existing.getUserId())) {
      throw new IllegalArgumentException("Wrong user for entity");
    }
    repo.deleteById(id);
  }

  private void applyCostPerUnit(Packaging p) {
    Integer qty = p.getQuantity();
    BigDecimal avg = p.getAverageCost();
    if (qty != null && qty > 0 && avg != null) {
      p.setCostPerUnit(
          avg.divide(BigDecimal.valueOf(qty), 4, RoundingMode.HALF_UP)
              .setScale(2, RoundingMode.HALF_UP));
    } else {
      p.setCostPerUnit(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));
    }
  }
}
