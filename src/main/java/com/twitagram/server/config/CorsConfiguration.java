package com.twitagram.server.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfiguration implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowedOrigins("http://localhost:3000","https://clone-fe-three.vercel.app","https://clone-l9oboc2et-ericagong.vercel.app")
//                .allowedOriginPatterns("*")
                .exposedHeaders("authorization","Access-Token-Expire-Time","authentication");
//                .allowCredentials(true);
    }
}