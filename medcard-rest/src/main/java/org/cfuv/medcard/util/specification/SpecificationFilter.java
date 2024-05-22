package org.cfuv.medcard.util.specification;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.cfuv.medcard.util.filter.FilterOperator;
import org.cfuv.medcard.util.filter.LogicalOperator;
import org.cfuv.medcard.util.filter.RequestFilter;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class SpecificationFilter<T> implements Specification<T> {

    protected final List<RequestFilter> filters;
    protected final Class<T> entityClass;
    private final Map<String, String> complexTypesMapping = new HashMap<>();

    public SpecificationFilter(Class<T> entityClass,
                               List<RequestFilter> filters,
                               Map<String, String> complexTypesMapping) {
        if (Objects.nonNull(complexTypesMapping)) {
            this.complexTypesMapping.putAll(complexTypesMapping);
        }
        this.entityClass = entityClass;
        this.filters = filters.stream()
                .peek(this::enrichFilter)
                .filter(this::filter)
                .collect(Collectors.toList());
    }

    public SpecificationFilter(Class<T> entityClass,
                               List<RequestFilter> filters) {
        this(entityClass, filters, null);
    }

    @Override
    public Predicate toPredicate(@Nonnull Root<T> root,
                                 @Nonnull CriteriaQuery<?> criteriaQuery,
                                 @Nonnull CriteriaBuilder criteriaBuilder) {
        Predicate[] pr = filters
                .stream()
                .map(filter -> makePredicate(root, criteriaBuilder, filter))
                .filter(Objects::nonNull)
                .toArray(Predicate[]::new);

        return compoundPredicates(criteriaBuilder, pr);
    }

    @Nullable
    private Predicate makePredicate(Root<T> root, CriteriaBuilder criteriaBuilder, RequestFilter filter) {
        String fieldName = filter.getField();
        Comparable value = filter.getValue();
        FilterOperator operator = filter.getOperator();
        if (Objects.isNull(value) || Objects.isNull(operator)) {
            log.info("Value or operator of {} is null. Mapping to null", fieldName);
            return criteriaBuilder.isNull(buildExpression(root, fieldName));
        }

        return switch (operator) {
            case EQUALS -> criteriaBuilder.equal(buildExpression(root, fieldName), value);
            case NOT_EQUALS -> criteriaBuilder.notEqual(buildExpression(root, fieldName), value);
            case GT -> criteriaBuilder.greaterThan(buildExpression(root, fieldName), value);
            case GT_OR_EQ -> criteriaBuilder.greaterThanOrEqualTo(buildExpression(root, fieldName), value);
            case LT -> criteriaBuilder.lessThan(buildExpression(root, fieldName), value);
            case LT_OR_EQ -> criteriaBuilder.lessThanOrEqualTo(buildExpression(root, fieldName), value);
            case IN -> buildExpression(root, fieldName).in(filter.getCollectionValue());
            case LIKE -> criteriaBuilder.like(buildExpression(root, fieldName), "%" + value + "%");
            case NOT_LIKE -> criteriaBuilder.notLike(buildExpression(root, fieldName), "%" + value + "%");
            case START_WITH -> criteriaBuilder.equal(
                    criteriaBuilder.substring(buildExpression(root, fieldName), 1, value.toString().length()), value
            );
        };
    }

    private Expression buildExpression(Root<T> root, String fieldName) {
        if (fieldName.contains(".")) {
            String[] splitFieldName = splitByDot(fieldName);
            return root.get(splitFieldName[0]).get(splitFieldName[1]);
        } else {
            return root.get(fieldName);
        }
    }

    protected Predicate compoundPredicates(CriteriaBuilder criteriaBuilder, Predicate[] pr) {
        Predicate predicate;

        if (filters.size() < 2 || Objects.isNull(filters.get(0).getLogicalOperator())) {
            return criteriaBuilder.or(pr);
        } else {
            int prLastIndex = pr.length - 1;

            if (LogicalOperator.OR.equals(filters.get(prLastIndex - 1).getLogicalOperator())) {
                predicate = criteriaBuilder.or(pr[prLastIndex], pr[prLastIndex - 1]);
            } else {
                predicate = criteriaBuilder.and(pr[prLastIndex], pr[prLastIndex - 1]);
            }
            for (int i = prLastIndex - 2; i >= 0; i--) {
                if (LogicalOperator.OR.equals(filters.get(i).getLogicalOperator())) {
                    predicate = criteriaBuilder.or(predicate, pr[i]);
                } else {
                    predicate = criteriaBuilder.and(predicate, pr[i]);
                }
            }
        }

        return predicate;
    }

    protected boolean filter(RequestFilter requestFilter) {
        try {
            final Class<?> requiredValueType = getFieldType(entityClass, requestFilter.getField());
            if (FilterOperator.IN.equals(requestFilter.getOperator())) {
                Collection<?> values = requestFilter.getCollectionValue();
                if (Objects.nonNull(values)) {
                    boolean isAllMatches = true;
                    for (Object value : values) {
                        final Class<?> filterValueType = value.getClass();
                        isAllMatches = isAllMatches && filterValueType.equals(requiredValueType);
                    }
                    return isAllMatches;
                } else {
                    return false;
                }
            } else {
                final Class<?> filterValueType = requestFilter.getValue().getClass();
                return filterValueType.equals(requiredValueType);
            }

        } catch (NoSuchFieldException e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    private String[] splitByDot(String filterFieldName) {
        return filterFieldName.split("\\.");
    }

    protected void enrichFilter(RequestFilter requestFilter) {
        try {
            requestFilter = enrichFilterCustom(requestFilter);
            if (complexTypesMapping.containsKey(requestFilter.getField())) {
                requestFilter.setField(complexTypesMapping.get(requestFilter.getField()));
            }

            Class<?> classFieldType = getFieldType(entityClass, requestFilter.getField());
            if (Objects.nonNull(classFieldType)) {
                setFilterValue(requestFilter, classFieldType, requestFilter.getValue());
            }
        } catch (NoSuchFieldException e) {
            log.error(e.getMessage(), e);
        }
    }

    private Class<?> getFieldType(Class<?> classToCheck, String filterFieldName) throws NoSuchFieldException {
        if (filterFieldName.contains(".")) {
            return getSubclassFieldType(classToCheck, splitByDot(filterFieldName)[0], splitByDot(filterFieldName)[1]);
        } else {
            return getFieldType(classToCheck, filterFieldName, 0);
        }
    }

    private Class<?> getSubclassFieldType(Class<?> classToCheck,
                                          String classFieldName,
                                          String subClassFieldName) throws NoSuchFieldException {
        if (isClassContainsField(classToCheck, classFieldName)) {
            Class<?> subClass = classToCheck.getDeclaredField(classFieldName).getType();
            if (isClassContainsField(subClass, subClassFieldName)) {
                return subClass.getDeclaredField(subClassFieldName).getType();
            }
        }
        return null;
    }

    private Class<?> getFieldType(Class<?> classToCheck, String filterFieldName, int currentDepth)
            throws NoSuchFieldException {
        if (!Object.class.equals(classToCheck)) {
            if (isClassContainsField(classToCheck, filterFieldName)) {
                return classToCheck.getDeclaredField(filterFieldName).getType();
            } else {
                return getFieldType(classToCheck.getSuperclass(), filterFieldName, currentDepth + 1);
            }
        } else {
            return null;
        }
    }


    private boolean isClassContainsField(Class<?> classToCheck, String fieldName) {
        return Arrays.stream(classToCheck.getDeclaredFields()).anyMatch(field -> fieldName.equals(field.getName()));
    }

    private void setFilterValue(RequestFilter requestFilter, Class<?> entityFieldType, Comparable value) {
        if (FilterOperator.IN.equals(requestFilter.getOperator())) {
            requestFilter.setCollectionValue(getCollection(value.toString(), entityFieldType));
        } else {
            requestFilter.setValue(getProperValue(value, entityFieldType));
        }
    }

    private List<?> getCollection(String value, Class<?> entityFieldType) {
        return Arrays.stream(value.split(";"))
                .map((Comparable el) -> getProperValue(el, entityFieldType))
                .collect(Collectors.toList());
    }

    private Comparable getProperValue(Comparable value, Class<?> entityFieldType) {
        if (Long.class.equals(entityFieldType)) {
            return Long.parseLong(value.toString());
        } else if (Integer.class.equals(entityFieldType)) {
            return Integer.parseInt(value.toString());
        } else if (Instant.class.equals(entityFieldType)) {
            return Instant.ofEpochMilli(Long.parseLong(value.toString()));
        } else if (entityFieldType.isEnum()) {
            return Enum.valueOf(entityFieldType.asSubclass(Enum.class), value.toString());
        } else if (LocalDate.class.equals(entityFieldType)) {
            return LocalDate.parse(value.toString());
        }
        return value;
    }

    protected RequestFilter enrichFilterCustom(RequestFilter requestFilter) {
        return requestFilter;
    }

}
