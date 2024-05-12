package org.cfuv.medcard.dto;

import jakarta.validation.constraints.NotNull;
import org.cfuv.medcard.model.parameter.TemplateParameter;

import java.util.List;

public record AnalysisTemplateRequest(@NotNull String name,
                                      List<TemplateParameter> parameters) {
}
