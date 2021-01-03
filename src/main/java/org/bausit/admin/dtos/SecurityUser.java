package org.bausit.admin.dtos;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.bausit.admin.models.Participant;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@RequiredArgsConstructor
@Value
public class SecurityUser implements UserDetails {
    private final Participant participant;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return participant.getPermissions();
    }

    @Override
    public String getPassword() {
        return participant.getPassword();
    }

    @Override
    public String getUsername() {
        return participant.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
