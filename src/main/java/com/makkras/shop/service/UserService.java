package com.makkras.shop.service;

import com.makkras.shop.entity.User;

public interface UserService {
    boolean checkIfUserIsValidForRegistration(String login, String email);
    void registerUser(User user);
}
