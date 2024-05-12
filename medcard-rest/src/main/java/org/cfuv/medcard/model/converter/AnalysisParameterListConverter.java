package org.cfuv.medcard.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.cfuv.medcard.model.parameter.AnalysisParameter;
import org.cfuv.medcard.util.JsonUtils;

import java.util.List;

@Converter
public class AnalysisParameterListConverter implements AttributeConverter<List<AnalysisParameter>, String> {

    @Override
    public String convertToDatabaseColumn(List<AnalysisParameter> analysisParameters) {
        return JsonUtils.writeValue(analysisParameters);
    }

    @Override
    public List<AnalysisParameter> convertToEntityAttribute(String parameters) {
        return JsonUtils.readCollectionFromString(parameters, AnalysisParameter.class);
    }
}
