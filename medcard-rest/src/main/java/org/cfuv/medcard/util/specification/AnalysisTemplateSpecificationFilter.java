package org.cfuv.medcard.util.specification;

import org.cfuv.medcard.model.AnalysisTemplate;
import org.cfuv.medcard.util.filter.RequestFilter;

import java.util.List;

public class AnalysisTemplateSpecificationFilter extends SpecificationFilter<AnalysisTemplate> {

    public AnalysisTemplateSpecificationFilter(List<RequestFilter> filters) {
        super(AnalysisTemplate.class, filters);
    }

}
