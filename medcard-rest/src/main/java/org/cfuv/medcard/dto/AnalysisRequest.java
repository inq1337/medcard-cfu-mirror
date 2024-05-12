package org.cfuv.medcard.dto;

import org.cfuv.medcard.model.parameter.AnalysisParameter;

import java.time.LocalDate;
import java.util.List;

public record AnalysisRequest(String name,
                              String templateName,
                              LocalDate analysisDate,
                              List<AnalysisParameter> parameters,
                              String commentary) {
}
