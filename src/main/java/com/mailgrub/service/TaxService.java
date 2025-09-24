package com.mailgrub.service;

import com.mailgrub.model.Tax;
import org.springframework.data.domain.Page;

public interface TaxService {
  Page<Tax> findPage(String tenantId, String jurisdiction, int page, int size);

  Tax create(String tenantId, Tax in);

  Tax update(String tenantId, Integer id, Tax patch);

  void delete(String tenantId, Integer id);
}
