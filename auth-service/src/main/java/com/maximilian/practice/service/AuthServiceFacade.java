package com.maximilian.practice.service;

import com.maximilian.practice.domain.auth.AuthRequest;
import com.maximilian.practice.domain.auth.AuthInfoResponse;
import com.maximilian.practice.model.UserCredentials;
import com.maximilian.practice.rest.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthServiceFacade {

    private final TokenService tokenService;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;

    public String register(AuthRequest authRequest, String issuerUrl) {
        log.info("Registering with request " + authRequest);
        UserCredentials registeredUser = userDetailsService.register(authRequest);
        return tokenService.createTokenFor(registeredUser, issuerUrl);
    }

    public String login(AuthRequest authRequest, String issuerUrl) {
        log.info("Logging in with request " + authRequest);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password())
        );
        if (!authentication.isAuthenticated()) {
            throw new GeneralException("Invalid credentials");
        }
        return tokenService.createTokenFor(userDetailsService.findByUsername(authRequest.username()), issuerUrl);
    }

    public AuthInfoResponse getAuthInfoFromToken(String token) {
        log.info("Validating token and getting info from token...");
        return tokenService.getAuthInfoFromToken(token);
    }

}
