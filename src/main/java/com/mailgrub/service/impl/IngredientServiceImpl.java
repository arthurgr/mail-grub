package com.mailgrub.service.impl;

import com.mailgrub.model.Ingredient;
import com.mailgrub.repository.IngredientRepository;
import com.mailgrub.service.IngredientService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class IngredientServiceImpl implements IngredientService {

  private final IngredientRepository repo;

  public IngredientServiceImpl(IngredientRepository repo) {
    this.repo = repo;
  }

  @Override
  @Cacheable(value = "ingredients", key = "#tenantId + ':' + #name + ':' + #page + ':' + #size")
  public Page<Ingredient> findPage(String tenantId, String name, int page, int size) {
    var pageable = PageRequest.of(page, size);
    if (name == null || name.isBlank()) {
      return repo.findByTenantId(tenantId, pageable);
    }
    return repo.findByTenantIdAndNameContainingIgnoreCase(tenantId, name, pageable);
  }

  @Override
  @CacheEvict(value = "ingredients", key = "#tenantId + ':*'", allEntries = true)
  public Ingredient create(String tenantId, Ingredient in) {
    in.setId(null);
    in.setTenantId(tenantId);
    return repo.save(in);
  }

  @Override
  @CacheEvict(value = "ingredients", key = "#tenantId + ':*'", allEntries = true)
  public Ingredient update(String tenantId, Integer id, Ingredient patch) {
    var existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    if (!tenantId.equals(existing.getTenantId())) {
      throw new IllegalArgumentException("Wrong tenant for entity");
    }
    existing.setName(patch.getName());
    existing.setMeasurementType(patch.getMeasurementType());
    existing.setPurchaseSize(patch.getPurchaseSize());
    existing.setAverageCost(patch.getAverageCost());
    return repo.save(existing);
  }

  @Override
  @CacheEvict(value = "ingredients", key = "#tenantId + ':*'", allEntries = true)
  public void delete(String tenantId, Integer id) {
    var existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    if (!tenantId.equals(existing.getTenantId())) {
      throw new IllegalArgumentException("Wrong tenant for entity");
    }
    repo.deleteById(id);
  }
}
