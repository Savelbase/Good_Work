package com.rm.toolkit.feedbackcommandapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableCaching
@EnableEurekaClient
@EnableKafka
public class FeedbackCommandApplication {

    public static void main(String[] args) {
        SpringApplication.run(FeedbackCommandApplication.class, args);
    }
}
