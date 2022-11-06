package com.makkras.shop.service;

import com.makkras.shop.entity.User;
import com.makkras.shop.exception.CustomServiceException;

import java.util.Optional;

public interface UserService {
    boolean checkIfUserIsValidForRegistration(String login, String email);
    void registerUser(User user);
    void updateUserLogin(String oldLogin,String newLogin) throws CustomServiceException;
    boolean checkIfUserWithSuchLoginIsAlreadyPresent(String login);
    boolean checkIfUserWithSuchEmailIsAlreadyPresent(String email);
    void updateUserEmail(String newEmail,String oldEmail, String login) throws CustomServiceException;
    void updateUserPassword(String newPassword, String login) throws CustomServiceException;
    Optional<User> findActiveUserByLogin(String login);
}
