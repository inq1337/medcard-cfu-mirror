package org.cfuv.medcard.service.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.cfuv.medcard.api.repository.CardUserRepository;
import org.cfuv.medcard.api.service.RegistrationService;
import org.cfuv.medcard.dto.security.SignUpRequest;
import org.cfuv.medcard.model.user.CardUser;
import org.cfuv.medcard.model.user.UserRole;
import org.cfuv.medcard.model.user.UserStatus;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final CardUserRepository cardUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    @Override
    public String signup(SignUpRequest request) {
        if (cardUserRepository.existsByEmail(request.email())) {
            throw new AuthenticationServiceException("User with email " + request.email() + " already exists");
        } else {
            return jwtService.generateToken(registerNewUser(request));
        }
    }

    private String registerNewUser(SignUpRequest request) {
        CardUser cardUser = new CardUser();
        cardUser.setEmail(request.email());
        cardUser.setStatus(UserStatus.ACTIVE);
        cardUser.setRole(UserRole.USER);
        cardUser.setFirstname(request.firstname());
        cardUser.setSurname(request.surname());
        cardUser.setPatronymic(request.patronymic());
        cardUser.setPassword(passwordEncoder.encode(request.password()));
        cardUserRepository.save(cardUser);
        return request.email();
    }

}
