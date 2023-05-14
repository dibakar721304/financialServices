package com.account.current.config;


import io.swagger.v3.oas.models.OpenAPI;
        import io.swagger.v3.oas.models.info.Contact;
        import io.swagger.v3.oas.models.info.Info;
        import io.swagger.v3.oas.models.info.License;
        import org.springframework.context.annotation.Bean;
        import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI openApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Financial services")
                        .description("Financial services APIs allows to create new accounts for existing customers")
                        .version("v1.0")
                        .contact(new Contact()
                                .name("Test"))
                        .termsOfService("TOC")
                        .license(new License().name("License").url("#"))
                );
    }
}
