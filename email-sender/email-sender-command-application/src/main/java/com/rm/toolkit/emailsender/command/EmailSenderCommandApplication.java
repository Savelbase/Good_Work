package com.rm.toolkit.emailsender.command;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
@EnableKafka
@EnableScheduling
public class EmailSenderCommandApplication {
    public static void main(String[] args) {
        SpringApplication.run(EmailSenderCommandApplication.class, args);
    }
}
