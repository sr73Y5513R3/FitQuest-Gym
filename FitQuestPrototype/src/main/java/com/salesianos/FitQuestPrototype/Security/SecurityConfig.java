package com.salesianos.FitQuestPrototype.Security;



import com.salesianos.FitQuestPrototype.Security.ExceptionHandling.JwtAccessDeniedHandler;

import com.salesianos.FitQuestPrototype.Security.ExceptionHandling.JwtAuthenticationEntryPoint;

import com.salesianos.FitQuestPrototype.Security.Jwt.Access.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;

import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.web.servlet.config.annotation.CorsRegistry;



@EnableWebSecurity

@RequiredArgsConstructor

@Configuration

@EnableGlobalMethodSecurity(prePostEnabled = true)

public class SecurityConfig {



    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    private final JwtAccessDeniedHandler accessDeniedHandler;



    @Bean

    AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {



        AuthenticationManagerBuilder authenticationManagerBuilder =

                http.getSharedObject(AuthenticationManagerBuilder.class);



        AuthenticationManager authenticationManager =

                authenticationManagerBuilder.authenticationProvider(authenticationProvider())

                        .build();



        return authenticationManager;

    }



    @Bean

    DaoAuthenticationProvider authenticationProvider() {

        DaoAuthenticationProvider p = new DaoAuthenticationProvider();



        p.setUserDetailsService(userDetailsService);

        p.setPasswordEncoder(passwordEncoder);

        return p;



    }





    @Bean

    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {



        http.csrf(csrf -> csrf.disable());

        http.cors(Customizer.withDefaults());

        http.sessionManagement((session) -> session

                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(excepz -> excepz

                .authenticationEntryPoint(authenticationEntryPoint)

                .accessDeniedHandler(accessDeniedHandler)

        );

        http.authorizeHttpRequests(authz -> authz

                .requestMatchers(HttpMethod.POST, "/auth/register/**", "/auth/login", "/auth/refresh/token", "/error", "activate/account").permitAll()

                .requestMatchers(HttpMethod.GET).permitAll()

                .requestMatchers(HttpMethod.PUT, "cliente/edit/**", "entrenador/edit/**", "realizado/**").authenticated()

                .requestMatchers(HttpMethod.POST, "valoracion/**", "nivel/**", "ejercicio/{idEjercicio}/material/**").authenticated()

                .requestMatchers("/me/admin", "usuario/baja/**").hasRole("ADMIN")

                .anyRequest().authenticated());





        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);





        http.headers(headers ->

                headers.frameOptions(frameOptions -> frameOptions.disable()));



        return http.build();

    }
}