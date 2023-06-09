package com.maximilian.practice.rest.clients;

import com.maximilian.practice.domain.auth.AuthInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-service",
        path = "/api/v1/auth")
public interface AuthServiceClient {

    @GetMapping("/authInfoFromToken")
    AuthInfoResponse getAuthInfoFromToken(@RequestParam(name = "token") String token);

}
