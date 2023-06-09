package com.maximilian.practice.config;

import com.maximilian.practice.domain.auth.Role;
import com.maximilian.practice.filter.JwtTokenVerifier;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenVerifier jwtTokenVerifier;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/v1/users/**")
                .hasAnyAuthority(
                        Role.USER.name(),
                        Role.ADMIN.name(),
                        Role.SYSTEM.name()
                )
                .antMatchers(HttpMethod.GET, "/api/v1/users/**")
                .permitAll()
                .antMatchers("/actuator/**")
                .hasAnyAuthority(
                        Role.ADMIN.name(),
                        Role.SYSTEM.name()
                )
                .anyRequest()
                .authenticated()
                .and()
                .addFilterAfter(jwtTokenVerifier, UsernamePasswordAuthenticationFilter.class)
                .httpBasic().disable()
                .formLogin().disable()
                .build();
    }

}
