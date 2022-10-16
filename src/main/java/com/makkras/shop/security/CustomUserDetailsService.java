package com.makkras.shop.security;

import com.makkras.shop.entity.User;
import com.makkras.shop.repo.UserJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserJpaRepository userJpaRepository;

    @Autowired
    public CustomUserDetailsService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userJpaRepository.findByLoginOrEmailAndIsActive(username,username,true)
                .orElseThrow(() -> new UsernameNotFoundException("Such user doesn't exist"));
        return SecurityUser.convertFormUserToSecurityUser(user);
    }
}
