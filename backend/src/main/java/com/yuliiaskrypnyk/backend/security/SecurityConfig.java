package com.yuliiaskrypnyk.backend.security;

import com.yuliiaskrypnyk.backend.model.user.AppUserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private static final String EXERCISE_API_PATH = "/api/exercises/**";

    @Value("${app.url}")
    private String appUrl;

    @Bean
    @SuppressWarnings("squid:S4502")
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(a -> a
                        .requestMatchers("/api/auth/me").authenticated()
                        .requestMatchers(HttpMethod.DELETE, EXERCISE_API_PATH).hasAuthority(AppUserRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.PUT, EXERCISE_API_PATH).hasAuthority(AppUserRole.ADMIN.toString())
                        .requestMatchers(HttpMethod.POST, EXERCISE_API_PATH).hasRole(AppUserRole.ADMIN.toString())
                        .requestMatchers("/api/users/**").hasAuthority(AppUserRole.ADMIN.toString())
                        .anyRequest().permitAll()
                )
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
                .exceptionHandling(e -> e
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
                .oauth2Login(o -> o.defaultSuccessUrl(appUrl))
                .logout(l -> l.logoutSuccessUrl(appUrl));
        return http.build();
    }
}