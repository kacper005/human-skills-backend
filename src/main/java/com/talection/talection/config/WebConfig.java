package com.talection.talection.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class for CORS settings.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${cors.allowed.origins}")
    private String[] allowedOrigins;

    private final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    /**
     * Configures CORS settings for the application.
     *
     * <p>Allows all origins, methods, and headers. The response can be cached by clients for 1 hour.
     *
     * @param registry the CORS registry to configure
     */
    @Override
    public void addCorsMappings(@NonNull CorsRegistry registry) {

        logger.info("CORS allowed origins: {}", (Object) allowedOrigins);

        registry
                .addMapping("/**") //all endpoints
                .allowedOrigins(allowedOrigins) // "*" for all origins, or specific origins
                .allowedMethods("GET", "POST", "PUT", "DELETE") //allow HTTP request methods
                .allowedHeaders("*") //allow all headers
                .allowCredentials(true)
                .maxAge(3600 * 3L); //response can be cached by clients for 3 hours
    }
}