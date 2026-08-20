package com.microservice.submissions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI submissionsOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Submissions Service API")
                        .description("Submissions microservice for the Laundry example")
                        .version("v1"));
    }
}
