package com.makkras.shop.security;

import com.makkras.shop.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class SecurityUser implements UserDetails {
    private final String username;
    private final String password;
    private final List<SimpleGrantedAuthority> grantedAuthoritiesList;
    private final boolean isActive;

    public SecurityUser(String username, String password,
                        List<SimpleGrantedAuthority> grantedAuthoritiesList, boolean isActive) {
        this.username = username;
        this.password = password;
        this.grantedAuthoritiesList = grantedAuthoritiesList;
        this.isActive = isActive;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthoritiesList;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActive;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActive;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActive;
    }

    @Override
    public boolean isEnabled() {
        return isActive;
    }

    public static UserDetails convertFormUserToSecurityUser(User user) {
        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), user.isActive(),
                user.isActive(), user.isActive(), user.isActive(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRoleType().getRoleName())));
    }
}
