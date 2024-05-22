package org.cfuv.medcard.util.filter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class RequestFilter {
    private String field;
    private Comparable value;
    private Collection<?> collectionValue;
    private FilterOperator operator;
    private LogicalOperator logicalOperator;
}
