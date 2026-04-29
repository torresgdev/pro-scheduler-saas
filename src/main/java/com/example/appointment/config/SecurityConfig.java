package com.example.appointment.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)  // Desabilita CSRF
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/tenants/**").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger/ui/**").permitAll()
                        .anyRequest().authenticated() // oque nao for acima authentica
                )
                .build();
    }
}
