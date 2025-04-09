package org.bbqqvv.backendeducation.service.impl;

import lombok.Getter;
import org.bbqqvv.backendeducation.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
public class CustomUserDetailsImpl implements UserDetails {

    private final User user;
    private final Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetailsImpl(User user, Collection<? extends GrantedAuthority> authorities) {
        this.user = user;
        this.authorities = authorities;
    }

    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override public String getPassword() {
        return user.getPassword();
    }

    @Override public String getUsername() {
        return user.getEmail();
    }

    @Override public boolean isAccountNonExpired() {
        return true;
    }

    @Override public boolean isAccountNonLocked() {
        return true;
    }

    @Override public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override public boolean isEnabled() {
        return true;
    }
}
