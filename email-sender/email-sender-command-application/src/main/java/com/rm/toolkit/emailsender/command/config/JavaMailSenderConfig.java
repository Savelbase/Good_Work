package com.rm.toolkit.emailsender.command.config;

import com.rm.toolkit.emailsender.command.config.mail_properties.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
@EnableConfigurationProperties({MailProperties.class, MailCommonProperties.class})
@RequiredArgsConstructor
public class JavaMailSenderConfig {

    private final MailProperties mailProperties;
    private final MailCommonProperties mailCommonProperties;

    @Bean
    public JavaMailSender getJavaMailSender() {

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(mailProperties.getHost());
        mailSender.setPort(mailProperties.getPort());
        mailSender.setUsername(mailProperties.getUserName());
        mailSender.setPassword(mailProperties.getPassword());

        Properties properties = mailSender.getJavaMailProperties();
        properties.put("mail.transport.protocol", mailCommonProperties.getTransport().getProtocol());
        properties.put("mail.smtp.auth", mailCommonProperties.getSmtp().isAuth());
        properties.put("mail.smtp.starttls.enable", mailCommonProperties.getSmtp().getStarttls().isEnable());
        properties.put("mail.debug", mailCommonProperties.isDebug());

        return mailSender;
    }
}
