package org.cfuv.medcard.api.service;

import org.cfuv.medcard.dto.security.SignUpRequest;

public interface RegistrationService {

    String signup(SignUpRequest requestDto);

}
