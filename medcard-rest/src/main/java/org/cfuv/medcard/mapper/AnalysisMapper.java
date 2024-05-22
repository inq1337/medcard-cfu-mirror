package org.cfuv.medcard.mapper;

import org.cfuv.medcard.dto.AnalysisRequest;
import org.cfuv.medcard.dto.AnalysisResponse;
import org.cfuv.medcard.dto.page.AnalysisPageResponse;
import org.cfuv.medcard.model.Analysis;
import org.cfuv.medcard.model.AnalysisTemplate;
import org.cfuv.medcard.model.user.CardUser;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnalysisMapper {

    AnalysisMapper INSTANCE = Mappers.getMapper(AnalysisMapper.class);

    @Mapping(target = "totalPages", source = "totalPages")
    @Mapping(target = "totalElements", source = "totalElements")
    @Mapping(target = "items", source = "content")
    AnalysisPageResponse toPageResponse(Page<Analysis> page);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "name", source = "request.name")
    @Mapping(target = "template", source = "template")
    @Mapping(target = "cardUser", source = "cardUser")
    @Mapping(target = "parameters", expression = "java(new ArrayList<>())")
    Analysis toEntity(AnalysisRequest request, CardUser cardUser, AnalysisTemplate template);

    @Mapping(source = "template.name", target = "templateName")
    @Mapping(source = "template.id", target = "templateId")
    @Mapping(source = "cardUser.id", target = "cardUserId")
    AnalysisResponse toDto(Analysis analysis);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "template", ignore = true)
    @Mapping(target = "deleted", ignore = true)
    @Mapping(target = "parameters", ignore = true)
    Analysis partialUpdate(AnalysisRequest request, @MappingTarget Analysis analysis);

}
