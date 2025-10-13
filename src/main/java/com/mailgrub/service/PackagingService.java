package com.mailgrub.service;

import com.mailgrub.model.Packaging;
import org.springframework.data.domain.Page;

public interface PackagingService {
  Page<Packaging> findPage(String userId, String packagingMaterials, int page, int size);

  Packaging create(String userId, Packaging in);

  Packaging update(String userId, Integer id, Packaging patch);

  void delete(String userId, Integer id);
}
