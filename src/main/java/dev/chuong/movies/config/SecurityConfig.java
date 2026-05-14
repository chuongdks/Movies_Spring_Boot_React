package dev.chuong.movies.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Tell Security to use the CORS configuration from WebConfig
                .cors(org.springframework.security.config.Customizer.withDefaults())
                .csrf(csrf -> csrf.disable()) // comment this out to test POSTMAN
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/**").permitAll()
//                        // 3. Keep auth routes public
//                        .requestMatchers("/api/v1/auth/**").permitAll()
//                        // 4. Temporary: Permit movies/sync while testing, or use .authenticated()
//                        .requestMatchers("/api/v1/movies/**", "/api/v1/libraries/**").permitAll()
                        .anyRequest().authenticated()
                );
        return http.build();
    }
}
