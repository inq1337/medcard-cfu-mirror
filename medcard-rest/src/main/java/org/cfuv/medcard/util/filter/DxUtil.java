package org.cfuv.medcard.util.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DxUtil {

    public static List<RequestFilter> mapStringToFilters(String searchString) {
        List<RequestFilter> filterList = new ArrayList<>();
        if (Objects.isNull(searchString) || searchString.isEmpty()) {
            return filterList;
        }

        if (searchString.contains(",and,") || searchString.contains(",or,")) {
            String[] filters = searchString.split("((?<=,and,)|(?=,and,))|((?<=,or,)|(?=,or,))");

            for (int i = 0; i < filters.length; i++) {
                if (!filters[i].equals(",or,") && !filters[i].equals(",and,")) {
                    if (i != filters.length - 1) {
                        filterList.add(
                                extractFilter(filters[i], LogicalOperator.fromString(filters[i + 1]))
                        );
                    } else {
                        filterList.add(
                                extractFilter(filters[i], LogicalOperator.OR)
                        );
                    }
                }
            }
        } else {
            filterList.add(
                    extractFilter(searchString, LogicalOperator.OR)
            );
        }

        return filterList;
    }

    private static RequestFilter extractFilter(String rawFilter,
                                               LogicalOperator logicalOperator) {
        String fieldName = rawFilter.split(",")[0];
        String fieldValue = rawFilter.substring(rawFilter.lastIndexOf(",") + 1);
        String method = rawFilter.split(",")[1];
        return prepareFilter(fieldName, fieldValue, method, logicalOperator);
    }


    private static RequestFilter prepareFilter(String fieldName,
                                               String fieldValue,
                                               String method,
                                               LogicalOperator logicalOperator) {
        RequestFilter.RequestFilterBuilder filterBuilder = RequestFilter.builder();

        if (Objects.nonNull(method)) {
            filterBuilder.operator(FilterOperator.fromString(method));
        }

        return filterBuilder
                .field(fieldName)
                .value(fieldValue)
                .logicalOperator(logicalOperator)
                .build();
    }

}
