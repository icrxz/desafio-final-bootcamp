package com.mercadolibre.frescos_api_grupo_2_w2.security;

import com.mercadolibre.frescos_api_grupo_2_w2.entities.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class DetailUserData implements UserDetails {

    private final Optional<User> user;

    public DetailUserData(Optional<User> user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        authorities.add(new SimpleGrantedAuthority(user.orElse(new User()).getRole()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.orElse(new User()).getPassword();
    }

    @Override
    public String getUsername() {
        return user.orElse(new User()).getEmail();
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
