package com.makkras.shop.validator;

import java.util.Map;

public interface UserDataValidator {
    boolean validateUserChangeLoginData(String newLogin);
    boolean validateUserChangePasswordData(String newPassword);
    boolean validateUserChangeEmailData(String newEmail);
    boolean validateUserRegistrationData(Map<String, String> formValues);
    boolean validateUserSearchData(String rawLogin, String rawEmail);
}
