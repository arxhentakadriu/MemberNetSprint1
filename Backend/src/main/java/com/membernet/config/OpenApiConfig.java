package com.membernet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI memberNetOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("MemberNet Sprint 2 API")
                        .version("2.0.0")
                        .description(
                                "REST API for MemberNet user accounts, "
                                + "authentication, associations, memberships, "
                                + "authorization, guardianships and payments."
                        )
                        .contact(new Contact()
                                .name("MemberNet Development Team"))
                        .license(new License()
                                .name("Private training project")));
    }
}