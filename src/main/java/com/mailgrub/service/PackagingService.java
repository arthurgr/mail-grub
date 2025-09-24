package com.mailgrub.service;

import com.mailgrub.model.Packaging;
import org.springframework.data.domain.Page;

public interface PackagingService {
  Page<Packaging> findPage(String tenantId, String packagingMaterials, int page, int size);

  Packaging create(String tenantId, Packaging in);

  Packaging update(String tenantId, Integer id, Packaging patch);

  void delete(String tenantId, Integer id);
}
