package org.cfuv.medcard.dto;

import org.cfuv.medcard.model.parameter.TemplateParameter;

import java.util.List;

public record AnalysisTemplateResponse(long id,
                                       String name,
                                       List<TemplateParameter> parameters,
                                       boolean deleted) {
}
