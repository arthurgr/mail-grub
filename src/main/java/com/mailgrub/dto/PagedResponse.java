package com.mailgrub.dto;

import java.util.List;

public record PagedResponse<T>(
        List<T> content,
        Meta meta
) {
    public record Meta(
            int page,
            int size,
            long totalElements,
            int totalPages,
            boolean last
    ) {}
}