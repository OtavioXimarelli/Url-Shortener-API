package com.otaviodev.Encurtador.de.URLs.Configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
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
