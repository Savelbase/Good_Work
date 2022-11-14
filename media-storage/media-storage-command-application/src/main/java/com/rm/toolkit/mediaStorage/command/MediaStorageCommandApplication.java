package com.rm.toolkit.mediaStorage.command;

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
public class MediaStorageCommandApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaStorageCommandApplication.class, args);
    }
}