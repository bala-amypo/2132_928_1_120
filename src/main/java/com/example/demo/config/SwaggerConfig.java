package com.example.demo.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        final String securitySchemeName = "BearerAuth";

        return new OpenAPI()
                // Server URL
                .servers(List.of(
                        new Server().url("https://9146.408procr.amypo.ai/")
                ))

                // 🔐 Tell Swagger that APIs need Authorization
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))

                // 🔐 Define JWT Bearer scheme
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name("Authorization")
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                );
    }
}