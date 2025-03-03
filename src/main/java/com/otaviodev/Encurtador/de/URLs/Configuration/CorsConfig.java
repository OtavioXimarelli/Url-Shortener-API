package com.otaviodev.Encurtador.de.URLs.Configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.beans.BeanProperty;

@Configuration
public class CorsConfig {
    @BeanProperty
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("https://encurtador-de-url-dun.vercel.app/")
                        .allowedMethods("GET", "OPTIONS", "POST")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }


}
