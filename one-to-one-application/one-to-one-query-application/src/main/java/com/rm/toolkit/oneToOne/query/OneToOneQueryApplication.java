package com.rm.toolkit.oneToOne.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
@EnableKafka
public class OneToOneQueryApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneToOneQueryApplication.class, args);
    }

}
