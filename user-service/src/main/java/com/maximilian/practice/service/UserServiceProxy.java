package com.maximilian.practice.service;

import com.maximilian.practice.domain.auth.AuthInfoResponse;
import com.maximilian.practice.domain.user.UserCreateRequest;
import com.maximilian.practice.model.User;
import com.maximilian.practice.rest.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class UserServiceProxy {

    private final UserService userService;

    public void follow(Long fromId, Long toId) {
        AuthInfoResponse currentUser = getCurrentUser();
        log.info("User {} is trying to follow user {}, current credentials {}", fromId, toId, currentUser);
        if (!currentUser.userId().equals(fromId)) {
            throw new GeneralException("Unauthorized follow action.", HttpStatus.UNAUTHORIZED);
        }
        userService.follow(fromId, toId);
    }

    public void unfollow(Long fromId, Long toId) {
        AuthInfoResponse currentUser = getCurrentUser();
        log.info("User {} is unfollowing user {}, current credentials {}", fromId, toId, currentUser);
        if (!currentUser.userId().equals(fromId)) {
            throw new GeneralException("Unauthorized follow action.", HttpStatus.UNAUTHORIZED);
        }
        userService.unfollow(fromId, toId);
    }

    public User create(UserCreateRequest request) {
        AuthInfoResponse currentUser = getCurrentUser();
        log.info("Creating user info for following registered user {}", currentUser);
        if (userService.existsById(currentUser.userId())) {
            throw new GeneralException("User already has user entry with this information.");
        }
        return userService.create(request, currentUser.userId());
    }

    private AuthInfoResponse getCurrentUser() {
        AuthInfoResponse authInfo = (AuthInfoResponse) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if (authInfo == null) {
            throw new GeneralException("Could not identify current user.", HttpStatus.UNAUTHORIZED);
        }
        return authInfo;
    }

}
