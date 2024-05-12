package org.cfuv.medcard.dto.page;

import lombok.Data;

import java.util.List;

@Data
public class PageResponse<T> {
    List<T> items;
    Integer totalPages;
    Long totalElements;
}