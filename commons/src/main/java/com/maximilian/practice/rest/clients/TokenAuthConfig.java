package com.maximilian.practice.rest.clients;

import org.springframework.context.annotation.Bean;

public class TokenAuthConfig {

    @Bean
    public TokenRequestInterceptor tokenRequestInterceptor() {
        return new TokenRequestInterceptor();
    }

}
