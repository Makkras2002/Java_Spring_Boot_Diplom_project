package com.makkras.shop.service.impl;

import com.makkras.shop.entity.User;
import com.makkras.shop.exception.CustomServiceException;
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
    public boolean checkIfUserIsValidForRegistration(String login, String email) {
        return !userJpaRepository.existsByLoginOrEmail(login,email);
    }
    @Override
    public boolean checkIfUserWithSuchLoginIsAlreadyPresent(String login) {
        return userJpaRepository.existsByLogin(login);
    }
    @Override
    public boolean checkIfUserWithSuchEmailIsAlreadyPresent(String email) {
        return userJpaRepository.existsByEmail(email);
    }
    @Override
    public void registerUser(User user){
        userJpaRepository.save(user);
    }

    @Override
    public void updateUserLogin(String newLogin,String oldLogin) throws CustomServiceException {
        if(userJpaRepository.updateUserLogin(newLogin,oldLogin) !=1) {
            throw new CustomServiceException("Error occurred during login update!");
        }
    }

    @Override
    public void updateUserEmail(String newEmail,String oldEmail, String login) throws CustomServiceException {
        if(userJpaRepository.updateUserEmail(newEmail,oldEmail,login) !=1) {
            throw new CustomServiceException("Error occurred during email update!");
        }
    }

    @Override
    public void updateUserPassword(String newPassword, String login) throws CustomServiceException {
        if(userJpaRepository.updateUserPassword(newPassword, login) !=1) {
            throw new CustomServiceException("Error occurred during password update!");
        }
    }
}
