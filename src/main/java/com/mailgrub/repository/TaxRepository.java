package com.mailgrub.repository;

import com.mailgrub.model.Tax;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRepository extends JpaRepository<Tax, Integer> {

  Page<Tax> findByTenantId(String tenantId, Pageable pageable);

  Page<Tax> findByTenantIdAndJurisdictionContainingIgnoreCase(
          String tenantId, String jurisdiction, Pageable pageable);
}
