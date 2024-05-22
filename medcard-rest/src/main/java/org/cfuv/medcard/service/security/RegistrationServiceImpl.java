package org.cfuv.medcard.service.security;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.cfuv.medcard.api.repository.CardUserRepository;
import org.cfuv.medcard.api.service.AnalysisTemplateService;
import org.cfuv.medcard.api.service.RegistrationService;
import org.cfuv.medcard.dto.security.SignUpRequest;
import org.cfuv.medcard.mapper.CardUserMapper;
import org.cfuv.medcard.model.user.CardUser;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RegistrationServiceImpl implements RegistrationService {

    private final CardUserRepository cardUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AnalysisTemplateService analysisTemplateService;

    @Transactional
    @Override
    public String signup(SignUpRequest request) {
        if (cardUserRepository.existsByEmail(request.email())) {
            throw new AuthenticationServiceException("User with email " + request.email() + " already exists");
        } else {
            CardUser cardUser = saveNewUser(request);
            analysisTemplateService.createStartTemplates(cardUser);
            return jwtService.generateToken(cardUser.getEmail());
        }
    }

    private CardUser saveNewUser(SignUpRequest request) {
        CardUser cardUser = CardUserMapper.INSTANCE.toEntity(request);
        cardUser.setPassword(passwordEncoder.encode(request.password()));
        return cardUserRepository.save(cardUser);
    }

}
