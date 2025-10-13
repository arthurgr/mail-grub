package com.mailgrub.service;

import com.mailgrub.model.Tax;
import org.springframework.data.domain.Page;

public interface TaxService {
  Page<Tax> findPage(String userId, String jurisdiction, int page, int size);

  Tax create(String userId, Tax in);

  Tax update(String userId, Integer id, Tax patch);

  void delete(String userId, Integer id);
}
