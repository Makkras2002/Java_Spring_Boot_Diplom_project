package com.makkras.shop.service.impl;

import com.makkras.shop.entity.User;
import com.makkras.shop.repo.UserJpaRepository;
import com.makkras.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService implements UserService {
    private final UserJpaRepository userJpaRepository;

    @Autowired
    public CustomUserService(UserJpaRepository userJpaRepository) {
        this.userJpaRepository = userJpaRepository;
    }

    @Override
    public boolean checkIfUserIsValidForRegistration(String login, String email){
        return !userJpaRepository.existsByLoginOrEmail(login,email);
    }
    @Override
    public void registerUser(User user){
        userJpaRepository.save(user);
    }
}
