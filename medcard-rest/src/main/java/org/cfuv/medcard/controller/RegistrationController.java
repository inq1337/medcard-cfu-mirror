package org.cfuv.medcard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cfuv.medcard.api.service.RegistrationService;
import org.cfuv.medcard.dto.security.JWTResponse;
import org.cfuv.medcard.dto.security.SignUpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/signup")
    public ResponseEntity<JWTResponse> signup(@Valid @RequestBody SignUpRequest requestDto)
            throws AuthenticationException {
        String token = registrationService.signup(requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(new JWTResponse(token));
    }
}
