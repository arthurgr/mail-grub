package com.mailgrub.dto;

public class Meta {
  private final int page;
  private final int size;
  private final long totalElements;
  private final int totalPages;
  private final boolean last;

  public Meta(int page, int size, long totalElements, int totalPages, boolean last) {
    this.page = page;
    this.size = size;
    this.totalElements = totalElements;
    this.totalPages = totalPages;
    this.last = last;
  }

  public int getPage() {
    return page;
  }

  public int getSize() {
    return size;
  }

  public long getTotalElements() {
    return totalElements;
  }

  public int getTotalPages() {
    return totalPages;
  }

  public boolean isLast() {
    return last;
  }
}
