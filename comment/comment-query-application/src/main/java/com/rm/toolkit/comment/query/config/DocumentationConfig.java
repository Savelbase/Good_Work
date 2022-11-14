package com.rm.toolkit.comment.query.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DocumentationConfig {

    @Bean
    public OpenAPI customOpenApi() {
        return new OpenAPI().addSecurityItem(new SecurityRequirement().addList("Authorization"))
                .components(new Components()
                        .addSecuritySchemes("Authorization", new SecurityScheme()
                                .in(SecurityScheme.In.HEADER)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("Bearer")
                                .bearerFormat("JWT"))
                )
                .info(new Info().title("Comment Query API").description("Эндпоинты для получения комментариия"));
    }
}
