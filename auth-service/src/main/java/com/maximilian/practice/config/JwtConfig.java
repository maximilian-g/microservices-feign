package com.maximilian.practice.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class JwtConfig {

    @Value("${app.jwt.secret}")
    private String secret;
    @Value("${app.jwt.roles.key}")
    private String rolesKey;
    @Value("${app.jwt.user.id.key}")
    private String userIdKey;
    @Value("${app.jwt.access.token.prefix}")
    private String accessTokenPrefix;
    @Value("${app.jwt.access.token.expiration}")
    private Long accessTokenExpirationMS;

}
