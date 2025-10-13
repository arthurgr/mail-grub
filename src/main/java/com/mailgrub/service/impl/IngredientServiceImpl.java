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
  @Cacheable(value = "ingredients", key = "#userId + ':' + #name + ':' + #page + ':' + #size")
  public Page<Ingredient> findPage(String userId, String name, int page, int size) {
    var pageable = PageRequest.of(page, size);
    if (name == null || name.isBlank()) {
      return repo.findByUserId(userId, pageable);
    }
    return repo.findByUserIdAndNameContainingIgnoreCase(userId, name, pageable);
  }

  @Override
  @CacheEvict(value = "ingredients", key = "#userId + ':*'", allEntries = true)
  public Ingredient create(String userId, Ingredient in) {
    in.setId(null);
    in.setUserId(userId);
    return repo.save(in);
  }

  @Override
  @CacheEvict(value = "ingredients", key = "#userId + ':*'", allEntries = true)
  public Ingredient update(String userId, Integer id, Ingredient patch) {
    var existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    if (!userId.equals(existing.getUserId())) {
      throw new IllegalArgumentException("Wrong user for entity");
    }
    existing.setName(patch.getName());
    existing.setMeasurementType(patch.getMeasurementType());
    existing.setPurchaseSize(patch.getPurchaseSize());
    existing.setAverageCost(patch.getAverageCost());
    return repo.save(existing);
  }

  @Override
  @CacheEvict(value = "ingredients", key = "#userId + ':*'", allEntries = true)
  public void delete(String userId, Integer id) {
    var existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    if (!userId.equals(existing.getUserId())) {
      throw new IllegalArgumentException("Wrong user for entity");
    }
    repo.deleteById(id);
  }
}
