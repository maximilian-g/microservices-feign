package com.maximilian.practice.controller;

import com.maximilian.practice.domain.auth.AuthInfoResponse;
import com.maximilian.practice.domain.auth.AuthRequest;
import com.maximilian.practice.service.AuthServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@Validated
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceFacade authService;

    // should return token
    @PostMapping("/register")
    public String register(@RequestBody @Valid AuthRequest registerRequest, HttpServletRequest request) {
        return authService.register(registerRequest, request.getRequestURL().toString());
    }

    // should return token
    @PostMapping("/login")
    public String login(@RequestBody @Valid AuthRequest loginRequest, HttpServletRequest request) {
        return authService.login(loginRequest, request.getRequestURL().toString());
    }

    // will return AuthInfoResponse for given token
    @GetMapping("/authInfoFromToken")
    public AuthInfoResponse getAuthInfoFromToken(@RequestParam String token) {
        return authService.getAuthInfoFromToken(token);
    }

}
