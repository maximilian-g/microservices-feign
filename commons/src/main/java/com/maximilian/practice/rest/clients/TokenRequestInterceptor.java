package com.maximilian.practice.rest.clients;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
public class TokenRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest currentRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
            String authHeader = currentRequest.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null) {
                requestTemplate.header(HttpHeaders.AUTHORIZATION, authHeader);
            }
        }
    }

}
