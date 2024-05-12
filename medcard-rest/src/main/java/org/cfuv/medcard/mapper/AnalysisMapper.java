package org.cfuv.medcard.mapper;

import org.cfuv.medcard.dto.AnalysisResponse;
import org.cfuv.medcard.dto.page.AnalysisPageResponse;
import org.cfuv.medcard.model.Analysis;
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

//    @Mapping(source = "cardUserId", target = "cardUser.id")
//    @Mapping(source = "cardUserId", target = "cardUser.id")
//    Analysis toEntity(AnalysisRequest analysisResponse);

    @Mapping(source = "template.name", target = "templateName")
    @Mapping(source = "template.id", target = "templateId")
    @Mapping(source = "cardUser.id", target = "cardUserId")
    AnalysisResponse toDto(Analysis analysis);

    @Mapping(source = "templateName", target = "template.name")
    @Mapping(source = "templateId", target = "template.id")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "cardUserId", target = "cardUser.id")
    Analysis partialUpdate(AnalysisResponse analysisResponse, @MappingTarget Analysis analysis);

}
