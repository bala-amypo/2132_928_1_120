package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF for APIs & tests
            .csrf(csrf -> csrf.disable())

            // Allow these endpoints without auth
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                        "/status",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/swagger-ui.html"
                ).permitAll()
                .anyRequest().authenticated()
            )

            // Use basic auth (safe default)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}