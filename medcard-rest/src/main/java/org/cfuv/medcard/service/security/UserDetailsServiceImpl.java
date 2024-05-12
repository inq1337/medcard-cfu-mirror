package org.cfuv.medcard.service.security;

import lombok.RequiredArgsConstructor;
import org.cfuv.medcard.api.repository.CardUserRepository;
import org.cfuv.medcard.exception.ObjectNotFoundException;
import org.cfuv.medcard.model.user.CardUser;
import org.cfuv.medcard.model.user.UserDetailsImpl;
import org.cfuv.medcard.model.user.UserStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CardUserRepository cardUserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        CardUser user = cardUserRepository.findByEmail(email)
                .orElseThrow(ObjectNotFoundException.supply(CardUser.class, email));
        boolean nonLocked = !UserStatus.BLOCKED.equals(user.getStatus());
        return new UserDetailsImpl(user, nonLocked);
    }
}
