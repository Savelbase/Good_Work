package com.rm.toolkit.emailsender.command.config.mail_properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("spring.mail")
@Data
public class MailProperties {

    private String host;
    private int port;
    private String userName;
    private String password;
}
