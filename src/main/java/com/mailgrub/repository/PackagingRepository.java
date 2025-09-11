package com.mailgrub.repository;

import com.mailgrub.model.Packaging;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface PackagingRepository
    extends ListCrudRepository<Packaging, Integer>, PagingAndSortingRepository<Packaging, Integer> {

  Page<Packaging> findByPackagingMaterialsContainingIgnoreCase(
      String packagingMaterials, Pageable pageable);
}
