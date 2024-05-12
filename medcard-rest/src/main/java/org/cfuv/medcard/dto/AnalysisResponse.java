package org.cfuv.medcard.dto;

import org.cfuv.medcard.model.parameter.AnalysisParameter;

import java.time.LocalDate;
import java.util.List;

public record AnalysisResponse(Long id,
                               String name,
                               Long templateId,
                               String templateName,
                               LocalDate analysisDate,
                               Long cardUserId,
                               List<AnalysisParameter> parameters,
                               String commentary,
                               List<String> images,
                               boolean deleted) {
}
