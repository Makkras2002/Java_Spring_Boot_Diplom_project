package com.makkras.shop.service;

import com.makkras.shop.entity.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

public interface MailService {
    void sendVerificationEmail(User user, String pageURL) throws MessagingException, UnsupportedEncodingException;
    void sendSuccessfulClientOrderEmail(User user, Optional<String> deliveryAddress) throws MessagingException, UnsupportedEncodingException;
    void sendPasswordRestoreCode(User user, Integer code) throws MessagingException, UnsupportedEncodingException;
}
