package com.makkras.shop.service;

import com.makkras.shop.entity.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MailService {
    void sendVerificationEmail(User user, String pageURL) throws MessagingException, UnsupportedEncodingException;
}
