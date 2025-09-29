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
          "#tenantId + ':' + (#jurisdiction == null ? '' : #jurisdiction) + ':' + #page + ':' + #size")
  public Page<Tax> findPage(String tenantId, String jurisdiction, int page, int size) {
    var pageable = PageRequest.of(page, size);
    if (jurisdiction == null || jurisdiction.isBlank()) {
      return repo.findByTenantId(tenantId, pageable);
    }
    return repo.findByTenantIdAndJurisdictionContainingIgnoreCase(tenantId, jurisdiction, pageable);
  }

  @Override
  @CacheEvict(cacheNames = "taxes", key = "#tenantId + ':*'", allEntries = true)
  public Tax create(String tenantId, Tax in) {
    in.setId(null);
    in.setTenantId(tenantId);
    return repo.save(in);
  }

  @Override
  @CacheEvict(cacheNames = "taxes", key = "#tenantId + ':*'", allEntries = true)
  public Tax update(String tenantId, Integer id, Tax patch) {
    var existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    if (!tenantId.equals(existing.getTenantId())) {
      throw new IllegalArgumentException("Wrong tenant for entity");
    }
    if (patch.getJurisdiction() != null) existing.setJurisdiction(patch.getJurisdiction());
    if (patch.getTaxRate() != null) existing.setTaxRate(patch.getTaxRate());
    return repo.save(existing);
  }

  @Override
  @CacheEvict(cacheNames = "taxes", key = "#tenantId + ':*'", allEntries = true)
  public void delete(String tenantId, Integer id) {
    var existing = repo.findById(id).orElseThrow(() -> new IllegalArgumentException("Not found"));
    if (!tenantId.equals(existing.getTenantId())) {
      throw new IllegalArgumentException("Wrong tenant for entity");
    }
    repo.deleteById(id);
  }
}
