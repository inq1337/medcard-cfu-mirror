package org.cfuv.medcard.mapper;

import org.cfuv.medcard.dto.AnalysisTemplateRequest;
import org.cfuv.medcard.dto.AnalysisTemplateResponse;
import org.cfuv.medcard.dto.page.AnalysisTemplatePageResponse;
import org.cfuv.medcard.model.AnalysisTemplate;
import org.cfuv.medcard.model.user.CardUser;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.NullValueCheckStrategy.ALWAYS;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(nullValueCheckStrategy = ALWAYS, unmappedTargetPolicy = IGNORE, componentModel = SPRING)
public interface AnalysisTemplateMapper {

    AnalysisTemplateMapper INSTANCE = Mappers.getMapper(AnalysisTemplateMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "parameters", expression = "java(new ArrayList<>())")
    AnalysisTemplate toEntity(AnalysisTemplateRequest request, CardUser cardUser);

    AnalysisTemplateResponse toDTO(AnalysisTemplate analysisTemplate);

    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "items", source = "content")
    AnalysisTemplatePageResponse toPageResponse(Page<AnalysisTemplate> page);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "parameters", expression = "java(request.parameters())")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    AnalysisTemplate partialUpdate(AnalysisTemplateRequest request, @MappingTarget AnalysisTemplate analysisTemplate);

}
