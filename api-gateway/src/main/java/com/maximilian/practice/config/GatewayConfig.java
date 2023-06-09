package com.maximilian.practice.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRouter(RouteLocatorBuilder builder) {
        return builder
                .routes()
                .route("user-api",
                        p -> p.path("/user/api/**")
                                .filters(f -> f.stripPrefix(1)) // will remove "/user" part, only "/api/**" will stay
                                .uri("lb://user-service")) // -> will route to http://user-service/api/v1/**
                .route("post-api",
                        p -> p.path("/post/api/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("lb://post-service"))
                .route("auth-api",
                        p -> p.path("/auth/api/**")
                                .filters(f -> f.stripPrefix(1))
                                .uri("lb://auth-service"))
                .build();
    }

}