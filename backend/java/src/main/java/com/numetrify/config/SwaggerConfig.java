package com.numetrify.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for setting up Swagger using SpringDoc.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configures the public API group for Swagger documentation.
     *
     * @return a GroupedOpenApi object for the public API
     *
     * Example usage:
     * <pre>
     * {@code
     * GroupedOpenApi publicApi = swaggerConfig.publicApi();
     * }
     * </pre>
     */
    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/api/**")
                .build();
    }
}