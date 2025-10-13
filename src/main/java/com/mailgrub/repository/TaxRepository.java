package com.mailgrub.repository;

import com.mailgrub.model.Tax;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaxRepository extends JpaRepository<Tax, Integer> {

  Page<Tax> findByUserId(String userId, Pageable pageable);

  Page<Tax> findByUserIdAndJurisdictionContainingIgnoreCase(
      String userId, String jurisdiction, Pageable pageable);
}
