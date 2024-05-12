package org.cfuv.medcard.model.user;

import com.google.common.collect.ImmutableSet;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@AllArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {

    private final String username;
    private final String password;
    private final Collection<? extends GrantedAuthority> authorities;
    private final boolean accountNonLocked;
    private final boolean accountNonExpired;
    private final boolean credentialsNonExpired;
    private final boolean enabled;

    public UserDetailsImpl(CardUser cardUser, boolean accountNonLocked) {
        this(
                cardUser.getEmail(),
                cardUser.getPassword(),
                ImmutableSet.of(new SimpleGrantedAuthority(cardUser.getRole().toString())),
                accountNonLocked,
                true, true, true
        );
    }

}
