package org.cfuv.medcard.mapper;

import org.cfuv.medcard.dto.CardUserProfileDTO;
import org.cfuv.medcard.model.user.CardUser;
import org.mapstruct.Mapper;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.factory.Mappers;

@Mapper(nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
public interface CardUserMapper {

    CardUserMapper INSTANCE = Mappers.getMapper(CardUserMapper.class);

    CardUserProfileDTO toDTO(CardUser user);

}
