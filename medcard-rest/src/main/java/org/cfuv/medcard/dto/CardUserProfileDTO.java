package org.cfuv.medcard.dto;

public record CardUserProfileDTO(String email,
                                 String firstname,
                                 String surname,
                                 String patronymic,
                                 String avatar) {
}
