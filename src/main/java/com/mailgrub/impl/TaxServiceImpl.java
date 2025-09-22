package com.mailgrub.impl;
import com.mailgrub.model.Tax;
import com.mailgrub.repository.TaxRepository;
import com.mailgrub.service.TaxService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TaxServiceImpl implements TaxService {

    private final TaxRepository repo;

    public TaxServiceImpl(TaxRepository repo) {
        this.repo = repo;
    }

    @Override
    @Cacheable(cacheNames = "taxSearch",
            key = "T(java.util.Objects).hash(#jurisdiction, #pageable.pageNumber, #pageable.pageSize)")
    public Page<Tax> find(String jurisdiction, Pageable pageable) {
        return repo.findByJurisdictionContainingIgnoreCase(jurisdiction, pageable);
    }

    @Override
    @Cacheable(cacheNames = "taxSearch",
            key = "T(java.util.Objects).hash('ALL', #pageable.pageNumber, #pageable.pageSize)")
    public Page<Tax> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    @Cacheable(cacheNames = "taxById", key = "#id")
    public Tax getById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @CachePut(cacheNames = "taxById", key = "#result.id", unless = "#result == null")
    @CacheEvict(cacheNames = "taxSearch", allEntries = true)
    public Tax save(Tax tax) {
        return repo.save(tax);
    }

    @Override
    @CacheEvict(cacheNames = { "taxById", "taxSearch" }, allEntries = true)
    public void deleteById(Integer id) {
        repo.deleteById(id);
    }
}
