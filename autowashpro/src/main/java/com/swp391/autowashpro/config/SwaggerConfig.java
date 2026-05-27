package com.swp391.autowashpro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customeOpenAPI(){
        Info info = new Info().title("AutoWashPRo")
                .description("AutoWash Pro – An intelligent automated car, bike" +
                        " wash management system with scheduling and loyalty programs.");

        return new OpenAPI().info(info);
    }
}
