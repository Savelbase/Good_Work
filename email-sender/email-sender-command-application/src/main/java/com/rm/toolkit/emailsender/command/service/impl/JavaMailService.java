package com.rm.toolkit.emailsender.command.service.impl;

import com.rm.toolkit.emailsender.command.model.Email;
import com.rm.toolkit.emailsender.command.service.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JavaMailService implements MailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendEmail(Email email) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email.getEmailTo());
        message.setFrom(from);
        message.setSubject(email.getSubject());
        message.setText(email.getText());

        javaMailSender.send(message);
        log.info("Email отправлен на почту: {}", email.getEmailTo());

    }
}