package com.rm.toolkit.auth.service.impl;

import com.rm.toolkit.auth.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@Profile("mail")
@RequiredArgsConstructor
@Slf4j
public class JavaMailService implements MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendPassword(String password, String email) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(from);
        message.setSubject("Информация об аккаунте Andersen RM.Toolkit");
        message.setText(String.format("Приветствую %s! Ваш пароль для входа в RM.Toolkit: %s", email, password));

        javaMailSender.send(message);
        log.info("Пароль отправлен на почту: {}", email);

    }
}