package com.mailgrub.service;

import com.mailgrub.model.Ingredient;
import com.mailgrub.repository.IngredientRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class IngredientServiceImpl implements IngredientService {

    private final IngredientRepository repo;

    public IngredientServiceImpl(IngredientRepository repo) {
        this.repo = repo;
    }

    @Override
    @Cacheable(cacheNames = "ingredientsSearch", key = "T(java.util.Objects).hash(#name, #pageable.pageNumber, #pageable.pageSize)")
    public Page<Ingredient> find(String name, Pageable pageable) {
        return repo.findByNameContainingIgnoreCase(name, pageable);
    }

    @Override
    @Cacheable(cacheNames = "ingredientsSearch", key = "T(java.util.Objects).hash('ALL', #pageable.pageNumber, #pageable.pageSize)")
    public Page<Ingredient> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    @Override
    @Cacheable(cacheNames = "ingredientsById", key = "#id")
    public Ingredient getById(Integer id) {
        return repo.findById(id).orElse(null);
    }

    @Override
    @CachePut(cacheNames = "ingredientsById", key = "#result.id", unless = "#result == null")
    @CacheEvict(cacheNames = "ingredientsSearch", allEntries = true)
    public Ingredient save(Ingredient ingredient) {
        return repo.save(ingredient);
    }

    @Override
    @CacheEvict(cacheNames = {"ingredientsById", "ingredientsSearch"}, allEntries = true)
    public void deleteById(Integer id) {
        repo.deleteById(id);
    }
}
