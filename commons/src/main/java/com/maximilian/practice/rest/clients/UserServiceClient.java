package com.maximilian.practice.rest.clients;

import com.maximilian.practice.domain.user.UserCreateRequest;
import com.maximilian.practice.domain.user.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service",
        path = "/api/v1/users",
        configuration = TokenAuthConfig.class)
public interface UserServiceClient {

    @PostMapping
    UserResponse createUser(@RequestBody UserCreateRequest user);

    @GetMapping("/{id}")
    UserResponse getById(@PathVariable(name = "id") Long id);

}
