package com.Clinic.WebApp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {
@Bean
public OpenAPI baseOpenApi(){
    return new OpenAPI().info(new Info().title("Animal Clinic - Swagger Docs")
            .version("1.1.0")
            .description("That's the documentation for our Animal Clinic API")
            .contact(new Contact().url("https://github.com/im-stanly/Animal-Clinic")));
    }
}
