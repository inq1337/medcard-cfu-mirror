package org.cfuv.medcard.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.cfuv.medcard.model.parameter.TemplateParameter;
import org.cfuv.medcard.util.JsonUtils;

import java.util.List;

@Converter
public class TemplateParameterListConverter implements AttributeConverter<List<TemplateParameter>, String> {

    @Override
    public String convertToDatabaseColumn(List<TemplateParameter> templateParameters) {
        return JsonUtils.writeValue(templateParameters);
    }

    @Override
    public List<TemplateParameter> convertToEntityAttribute(String parameters) {
        return JsonUtils.readCollectionFromString(parameters, TemplateParameter.class);
    }

}
