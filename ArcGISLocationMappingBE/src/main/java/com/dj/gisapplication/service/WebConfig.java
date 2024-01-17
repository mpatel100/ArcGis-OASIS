package com.dj.gisapplication.service;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
    	System.out.println("ADDING REGISTRY");
        registry.addMapping("/**") // This will enable CORS for all paths
                .allowedOrigins("http://localhost:4200") // Angular server's address
                .allowedMethods("GET", "POST", "PUT", "DELETE");
    }
}
