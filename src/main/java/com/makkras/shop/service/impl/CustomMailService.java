package com.makkras.shop.service.impl;

import com.makkras.shop.entity.User;
import com.makkras.shop.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Optional;

@Service
public class CustomMailService implements MailService {
    private static final String COMPANY_EMAIL = "max2002shpak@gmail.com";
    private static final String COMPANY_NAME = "BestFood";
    private static final String REGISTRATION_EMAIL_MESSAGE_SUBJECT = "Пожалуйста подтвердите свою регистрацию";
    private static final String SUCCESSFUL_CLIENT_ORDER_EMAIL_MESSAGE_SUBJECT = "Успешно совершённый заказ";
    private static final String REGISTRATION_EMAIL_MESSAGE_CONTENT = """
            
            Дорогой [[name]],<br>
            Пожалуйста пройдите по ссылке, чтобы подтвердить Вашу регистрацию:<br>
            <h3><a href=\"[[URL]]\"  target=\\"_self\\">VERIFY</a></h3>
            Мы Вам благодарны,<br>
            ООО \"BestFood\".""";
    private static final String SUCCESSFUL_CLIENT_ORDER_EMAIL_MESSAGE_CONTENT = """
            
            Дорогой [[name]],<br>
            Вы успешно осуществили заказ продуктов в магазине BestFood!<br>
            Ваш адрес доставки - [[DELIVERY]].<br>
            Мы Вам благодарны,<br>
            ООО \"BestFood\".""";
    private final JavaMailSender mailSender;

    @Autowired
    public CustomMailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(User user, String pageURL)
            throws MessagingException, UnsupportedEncodingException {
        String content = REGISTRATION_EMAIL_MESSAGE_CONTENT;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(COMPANY_EMAIL, COMPANY_NAME);
        helper.setTo(user.getEmail());
        helper.setSubject(REGISTRATION_EMAIL_MESSAGE_SUBJECT);

        content = content.replace("[[name]]", user.getLogin());

        content = content.replace("[[URL]]", pageURL);

        helper.setText(content, true);

        mailSender.send(message);

    }

    @Override
    public void sendSuccessfulClientOrderEmail(User user, Optional<String> deliveryAddress) throws MessagingException, UnsupportedEncodingException {
        String content = SUCCESSFUL_CLIENT_ORDER_EMAIL_MESSAGE_CONTENT;

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(COMPANY_EMAIL, COMPANY_NAME);
        helper.setTo(user.getEmail());
        helper.setSubject(SUCCESSFUL_CLIENT_ORDER_EMAIL_MESSAGE_SUBJECT);

        content = content.replace("[[name]]", user.getLogin());

        content = content.replace("[[DELIVERY]]", deliveryAddress.orElse("Самовывоз из магазина."));

        helper.setText(content, true);

        mailSender.send(message);
    }
}
