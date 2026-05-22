package com.example.securebank.config;

import org.springframework.context.annotation.Bean;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration

public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;

    }

    @Bean

    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }

    @Bean

    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http

            .csrf(csrf -> csrf.disable())

            .httpBasic(httpBasic -> httpBasic.disable())

            .formLogin(formLogin -> formLogin.disable())

            .sessionManagement(session ->

                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            )

            .authorizeHttpRequests(auth -> auth

                .requestMatchers(

                    "/",

                    "/index.html",

                    "/pages/**",

                    "/css/**",

                    "/js/**",

                    "/api/auth/**",

                    "/error"

                ).permitAll()

                .anyRequest().authenticated()

            )

            .addFilterBefore(

                jwtAuthenticationFilter,

                UsernamePasswordAuthenticationFilter.class

            );

        return http.build();

    }

}