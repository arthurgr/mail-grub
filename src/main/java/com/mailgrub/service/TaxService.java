package com.mailgrub.service;

import com.mailgrub.model.Tax;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaxService {
    Page<Tax> find(String jurisdiction, Pageable pageable);
    Page<Tax> findAll(Pageable pageable);
    Tax getById(Integer id);
    Tax save(Tax tax);
    void deleteById(Integer id);
}
