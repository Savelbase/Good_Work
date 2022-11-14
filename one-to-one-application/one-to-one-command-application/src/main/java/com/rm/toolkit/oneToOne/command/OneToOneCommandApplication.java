package com.rm.toolkit.oneToOne.command;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.kafka.annotation.EnableKafka;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
@EnableKafka
public class OneToOneCommandApplication {

    public static void main(String[] args) {
        SpringApplication.run(OneToOneCommandApplication.class, args);
    }

    //Чтобы в приложении использовать локальное время в часовом поясе UTC
    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }
}