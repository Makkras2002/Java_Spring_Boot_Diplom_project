package com.makkras.shop.security;

import com.makkras.shop.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class SecurityUser implements UserDetails {
    private final String username;
    private final String password;
    private final String email;
    private final Set<SimpleGrantedAuthority> grantedAuthoritiesList;
    private final boolean isActive;

    public SecurityUser(String username, String password, String email,
                        Set<SimpleGrantedAuthority> grantedAuthoritiesList, boolean isActive) {
        this.username = username;
        this.password = password;
        this.email = email;
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

    public String getEmail() {
        return email;
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
        return new SecurityUser(user.getLogin(), user.getPassword(),user.getEmail(),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRoleType().getRoleName())),
                user.isActive());
    }

    //    public static UserDetails convertFormUserToSecurityUser(User user) {
//        return new org.springframework.security.core.userdetails.User(user.getLogin(), user.getPassword(), user.isActive(),
//                user.isActive(), user.isActive(), user.isActive(),
//                Collections.singleton(new SimpleGrantedAuthority(user.getRole().getRoleType().getRoleName())));
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SecurityUser that = (SecurityUser) o;

        if (isActive != that.isActive) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;
        return grantedAuthoritiesList != null ? grantedAuthoritiesList.equals(that.grantedAuthoritiesList) : that.grantedAuthoritiesList == null;
    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (grantedAuthoritiesList != null ? grantedAuthoritiesList.hashCode() : 0);
        result = 31 * result + (isActive ? 1 : 0);
        return result;
    }
}
