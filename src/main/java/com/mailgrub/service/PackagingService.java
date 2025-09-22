package com.mailgrub.service;

import com.mailgrub.model.Packaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PackagingService {
    Page<Packaging> find(String packagingMaterials, Pageable pageable);
    Page<Packaging> findAll(Pageable pageable);
    Packaging getById(Integer id);
    Packaging save(Packaging packaging);
    void deleteById(Integer id);
}
