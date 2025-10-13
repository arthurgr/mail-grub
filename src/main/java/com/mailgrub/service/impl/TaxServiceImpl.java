package com.mailgrub.service.impl;

import com.mailgrub.model.Tax;
import com.mailgrub.repository.TaxRepository;
import com.mailgrub.service.TaxService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaxServiceImpl implements TaxService {

  private final TaxRepository repo;

  public TaxServiceImpl(TaxRepository repo) {
    this.repo = repo;
  }

  @Override
  @Cacheable(
      cacheNames = "taxes",
      key =
          "#userId + ':' + (#jurisdiction == null ? '' : #jurisdiction) + ':' + #page + ':' + #size")
  public Page<Tax> findPage(String userId, String jurisdiction, int page, int size) {
    var pageable = PageRequest.of(page, size);
    if (jurisdiction == null || jurisdiction.isBlank()) {
      return repo.findByUserId(userId, pageable);
    }
    return repo.findByUserIdAndJurisdictionContainingIgnoreCase(userId, jurisdiction, pageable);
  }

  @Override
  @CacheEvict(cacheNames = "taxes", key = "#userId + ':*'", allEntries = true)
  public Tax create(String userId, Tax in) {
    in.setId(null);
    in.setUserId(userId);
    return repo.save(in);
  }

  @Override
  @CacheEvict(cacheNames = "taxes", key = "#userId + ':*'", allEntries = true)
  public Tax update(String userId, Integer id, Tax patch) {
    var existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    if (!userId.equals(existing.getUserId())) {
      throw new IllegalArgumentException("Wrong user for entity");
    }
    if (patch.getJurisdiction() != null) existing.setJurisdiction(patch.getJurisdiction());
    if (patch.getTaxRate() != null) existing.setTaxRate(patch.getTaxRate());
    return repo.save(existing);
  }

  @Override
  @CacheEvict(cacheNames = "taxes", key = "#userId + ':*'", allEntries = true)
  public void delete(String userId, Integer id) {
    var existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    if (!userId.equals(existing.getUserId())) {
      throw new IllegalArgumentException("Wrong user for entity");
    }
    repo.deleteById(id);
  }
}
