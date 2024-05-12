package org.cfuv.medcard.util.specification;

import com.google.common.collect.ImmutableMap;
import org.cfuv.medcard.model.Analysis;
import org.cfuv.medcard.util.filter.RequestFilter;

import java.util.List;

public class AnalysisSpecificationFilter extends SpecificationFilter<Analysis> {

    public AnalysisSpecificationFilter(List<RequestFilter> filters) {
        super(Analysis.class, filters, ImmutableMap.of(
                "cardUserId", "cardUser.id"
        ));
    }

}
