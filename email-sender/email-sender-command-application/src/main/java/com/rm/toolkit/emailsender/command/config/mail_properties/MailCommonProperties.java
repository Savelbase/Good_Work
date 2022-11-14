package com.rm.toolkit.emailsender.command.config.mail_properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.mail.properties.mail")
@Data
public class MailCommonProperties {

    private Transport transport;
    private Smtp smtp;
    private boolean debug;

    @Data
    public static class Transport {
        private String protocol;
    }

    @Data
    public static class Smtp {
        private boolean auth;
        private Starttls starttls;

        @Data
        public static class Starttls {
            private boolean enable;
        }
    }
}
