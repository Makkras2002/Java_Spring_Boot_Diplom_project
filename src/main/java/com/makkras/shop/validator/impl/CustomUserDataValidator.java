package com.makkras.shop.validator.impl;

import com.makkras.shop.validator.UserDataValidator;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CustomUserDataValidator implements UserDataValidator {
    private static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    private static final String INVALID_DATA_ENTERED_ERROR = "Введены неверные данные!";
    private static final int MIN_LOGIN_LENGTH = 3;
    private static final int MAX_LOGIN_LENGTH = 100;
    private static final int MAX_EMAIL_LENGTH = 50;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final int MIN_PASSWORD_LENGTH = 8;

    @Override
    public boolean validateUserChangeLoginData(String newLogin) {
        boolean isValid = true;
        String login = newLogin;
        if (login.length() > MAX_LOGIN_LENGTH || login.length() < MIN_LOGIN_LENGTH) {
            isValid = false;
        }
        return isValid;
    }

    @Override
    public boolean validateUserChangePasswordData(String newPassword) {
        boolean isValid = true;
        String password = newPassword;
        if (password.length() > MAX_PASSWORD_LENGTH || password.length() < MIN_PASSWORD_LENGTH) {
            isValid = false;
        }
        return isValid;
    }

    @Override
    public boolean validateUserChangeEmailData(String newEmail) {
        boolean isValid = true;
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(newEmail);
        if (!matcher.matches()) {
            isValid = false;
        }
        return isValid;
    }

    @Override
    public boolean validateUserRegistrationData(Map<String, String> formValues) {
        boolean isValid = true;
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        String login = formValues.get("login");
        String email = formValues.get("email");
        String password = formValues.get("password");
        Matcher matcher = pattern.matcher(email);
        if (login.length() > MAX_LOGIN_LENGTH || login.length() < MIN_LOGIN_LENGTH) {
            formValues.replace("login", login, INVALID_DATA_ENTERED_ERROR);
            isValid = false;
        }
        if (password.length() > MAX_PASSWORD_LENGTH || password.length() < MIN_PASSWORD_LENGTH) {
            formValues.replace("password",password,
                    INVALID_DATA_ENTERED_ERROR);
            isValid = false;
        }
        if (!matcher.matches()) {
            formValues.replace("email",email,
                    INVALID_DATA_ENTERED_ERROR);
            isValid = false;
        }
        return isValid;
    }

    @Override
    public boolean validateUserSearchData(String rawLogin, String rawEmail) {
        boolean result = true;
        if (rawLogin.length() > MAX_LOGIN_LENGTH) {
            result = false;
        }
        if (rawEmail.length() > MAX_EMAIL_LENGTH) {
            result = false;
        }
        return result;
    }
}