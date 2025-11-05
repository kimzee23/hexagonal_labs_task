package org.enums.labs_hexagonal.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI enumOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Enum Labs API")
                        .description("Talent Authentication and Profile Management API")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Enum Labs Team")
                                .email("support@enumlabs.com")))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }
}