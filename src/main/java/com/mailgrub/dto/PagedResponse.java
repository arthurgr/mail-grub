package com.mailgrub.dto;

import org.springframework.data.domain.Page;
import java.util.List;

public class PagedResponse<T> {
  private final List<T> content;
  private final Meta meta;

  public PagedResponse(List<T> content, Meta meta) {
    this.content = content;
    this.meta = meta;
  }

  public List<T> getContent() {
    return content;
  }

  public Meta getMeta() {
    return meta;
  }

  public static <T> PagedResponse<T> fromPage(Page<T> page) {
    Meta meta = new Meta(
            page.getNumber(),
            page.getSize(),
            page.getTotalElements(),
            page.getTotalPages(),
            page.isLast()
    );
    return new PagedResponse<>(page.getContent(), meta);
  }
}
