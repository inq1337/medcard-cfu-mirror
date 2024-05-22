package org.cfuv.medcard.dto;

import org.cfuv.medcard.model.user.PrivilegeLevel;

public record CardUserProfileDTO(String email,
                                 String firstname,
                                 String surname,
                                 String patronymic,
                                 String avatar,
                                 PrivilegeLevel privilegeLevel) {
}
