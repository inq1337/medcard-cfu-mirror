package org.cfuv.medcard.mapper;

import org.cfuv.medcard.dto.CardUserProfileDTO;
import org.cfuv.medcard.dto.security.SignUpRequest;
import org.cfuv.medcard.model.user.CardUser;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface CardUserMapper {

    CardUserMapper INSTANCE = Mappers.getMapper(CardUserMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "privilegeLevel", ignore = true)
    CardUser toEntity(SignUpRequest request);

    CardUserProfileDTO toDto(CardUser cardUser);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    CardUser partialUpdate(CardUserProfileDTO cardUserProfileDTO, @MappingTarget CardUser cardUser);
}