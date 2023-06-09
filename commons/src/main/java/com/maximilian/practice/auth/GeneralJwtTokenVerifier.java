package com.maximilian.practice.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maximilian.practice.domain.auth.AuthInfoResponse;
import com.maximilian.practice.rest.RestResponse;
import com.maximilian.practice.rest.clients.AuthServiceClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@RequiredArgsConstructor
public abstract class GeneralJwtTokenVerifier extends OncePerRequestFilter {

    private final AuthServiceClient authServiceClient;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authString = request.getHeader(AUTHORIZATION);
        if (authString != null && !authenticate(authString, response)) {
            return;
        }
        filterChain.doFilter(request, response);
    }

    protected boolean authenticate(String authString, HttpServletResponse response) throws IOException {
        try {
            AuthInfoResponse authInfoResponse = authServiceClient.getAuthInfoFromToken(authString);
            setAuthentication(authInfoResponse);
            return true;
        } catch (RuntimeException ex) {
            // exceptions in filters are not handled by general controller advices
            log.info("Got error: " + ex.getMessage());
            RestResponse resp = new RestResponse();
            resp.setSuccess(false);
            resp.setMessage(ex.getMessage());
            response.getWriter().write(objectMapper.writeValueAsString(resp));
        }
        return false;
    }

    protected abstract void setAuthentication(AuthInfoResponse authInfoResponse);

}


