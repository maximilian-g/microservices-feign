package com.maximilian.practice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maximilian.practice.auth.GeneralJwtTokenVerifier;
import com.maximilian.practice.domain.auth.AuthInfoResponse;
import com.maximilian.practice.rest.clients.AuthServiceClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenVerifier extends GeneralJwtTokenVerifier {

    public JwtTokenVerifier(AuthServiceClient authServiceClient, ObjectMapper objectMapper) {
        super(authServiceClient, objectMapper);
    }

    @Override
    protected void setAuthentication(AuthInfoResponse authInfoResponse) {
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(authInfoResponse.username(),
                                authInfoResponse,
                                authInfoResponse.roles().stream().map(r -> new SimpleGrantedAuthority(r.name())).toList())
                );
    }

}
