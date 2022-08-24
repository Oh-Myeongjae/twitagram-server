package com.twitagram.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
//    public static final String ALLOWED_METHOD_NAMES = "GET,HEAD,POST,PUT,DELETE,TRACE,OPTIONS,PATCH";

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedMethods(ALLOWED_METHOD_NAMES.split(","))
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowedOrigins("http://localhost:3000","")
//                .allowedOrigins("*")
                .exposedHeaders("*")
                .allowCredentials(true);
    }
}