package com.example.rememberme.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openAPI(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("Remember Me API")
                    .description("API for Remember Me application")
                    .version("v1.0.0")
                    .contact(
                        Contact()
                            .name("Remember Me Team")
                            .email("contact@rememberme.com")
                    )
            )
            .addServersItem(
                Server()
                    .url("/api")
                    .description("Default Server URL")
            )
    }
}
