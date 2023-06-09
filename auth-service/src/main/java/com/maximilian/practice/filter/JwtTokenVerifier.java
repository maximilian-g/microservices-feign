package com.maximilian.practice.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maximilian.practice.config.JwtConfig;
import com.maximilian.practice.rest.RestResponse;
import com.maximilian.practice.rest.exception.GeneralException;
import com.maximilian.practice.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtConfig config;
    private final TokenService tokenService;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authString = request.getHeader(AUTHORIZATION);
        if (authString != null &&
                authString.startsWith(config.getAccessTokenPrefix()) &&
                !authenticate(authString, response)) {
            return;
        }
        filterChain.doFilter(request, response);
    }

    public boolean authenticate(String authString, HttpServletResponse response) throws IOException {
        try {
            authString = authString.substring(config.getAccessTokenPrefix().length());
            SecurityContextHolder.getContext()
                    .setAuthentication(tokenService.getAuthenticationFromToken(authString));
            return true;
        } catch (Exception ex) {
            // exceptions in filters are not handled by general controller advices
            log.error("Error during authentication: " + ex.getMessage());
            RestResponse resp = new RestResponse();
            resp.setSuccess(false);
            resp.setMessage(ex.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(resp));
        }
        return false;
    }

}
