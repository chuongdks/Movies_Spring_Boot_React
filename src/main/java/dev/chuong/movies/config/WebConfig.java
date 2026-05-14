package dev.chuong.movies.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Value("${app.cors.origins}")
    private String[] allowedOrigins;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //.allowedOrigins(allowedOrigins) // specific origin
                .allowedOriginPatterns("http://localhost:[*]", "http://127.0.0.1:[*]", "http://172.*:[*]") // test only, remember to comment this line and uncomment the line above
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true); // for future auth features (Steam OAuth)
    }
}

