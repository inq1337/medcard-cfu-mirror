package org.cfuv.medcard.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.cfuv.medcard.dto.security.AuthRequest;
import org.cfuv.medcard.dto.security.JWTResponse;
import org.cfuv.medcard.service.security.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/auth")
    public ResponseEntity<JWTResponse> login(@Valid @RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password()));
        String token = jwtService.generateToken(request.email(), request.rememberMe());
        return ResponseEntity.ok(new JWTResponse(token));
    }

}
