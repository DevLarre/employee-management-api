package com.example.employee_manager_api.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI().info(new Info()
                .title("Employee Manager API")
                .version("1.0.0")
                .description("API for employee management")
                .contact(new Contact()
                        .name("Your Name")
                        .email("yourmail@email.com")));
    }
}
