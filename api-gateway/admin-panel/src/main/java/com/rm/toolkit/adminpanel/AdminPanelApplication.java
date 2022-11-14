package com.rm.toolkit.adminpanel;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableAdminServer
@EnableDiscoveryClient
public class AdminPanelApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(AdminPanelApplication.class)
                .web(WebApplicationType.REACTIVE)
                .run(args);
    }
}
