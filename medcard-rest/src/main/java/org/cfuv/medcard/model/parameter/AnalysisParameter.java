package org.cfuv.medcard.model.parameter;

public record AnalysisParameter(String name,
                                String value,
                                String unit,
                                ParameterState state) {
}
