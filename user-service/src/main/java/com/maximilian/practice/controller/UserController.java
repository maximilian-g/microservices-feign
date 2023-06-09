package com.maximilian.practice.controller;

import com.maximilian.practice.domain.user.UserCreateRequest;
import com.maximilian.practice.domain.user.UserResponse;
import com.maximilian.practice.mapper.UserResponseMapper;
import com.maximilian.practice.model.User;
import com.maximilian.practice.rest.RestResponse;
import com.maximilian.practice.service.UserService;
import com.maximilian.practice.service.UserServiceProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Validated
public class UserController {

    private final UserService userService;
    private final UserServiceProxy userServiceProxy;

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Long id) {
        User user = userService.getById(id);
        return UserResponseMapper.map(user);
    }

    @GetMapping("/{id}/following")
    public List<UserResponse> getFollowingById(@PathVariable Long id) {
        List<User> users = userService.getFollowingByUserId(id);
        return users.stream()
                .map(UserResponseMapper::map)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}/followers")
    public List<UserResponse> getFollowersById(@PathVariable Long id) {
        List<User> users = userService.getFollowersByUserId(id);
        return users.stream()
                .map(UserResponseMapper::map)
                .collect(Collectors.toList());
    }

    @GetMapping
    public List<UserResponse> getUsers() {
        return userService.getAll().stream()
                .map(UserResponseMapper::map)
                .collect(Collectors.toList());
    }

    @PostMapping
    public UserResponse create(@Valid @RequestBody UserCreateRequest request) {
        User user = userServiceProxy.create(request);
        return UserResponseMapper.map(user);
    }

    @PostMapping("/{fromId}/follow/{toId}")
    public RestResponse follow(@PathVariable Long fromId,
                               @PathVariable Long toId) {
        userServiceProxy.follow(fromId, toId);
        return new RestResponse("Successfully added to following list.");
    }

    @PostMapping("/{fromId}/unfollow/{toId}")
    public RestResponse unfollow(@PathVariable Long fromId,
                                 @PathVariable Long toId) {
        userServiceProxy.unfollow(fromId, toId);
        return new RestResponse("Successfully removed from following list.");
    }

}
