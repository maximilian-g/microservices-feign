package com.maximilian.practice.service;

import com.maximilian.practice.domain.auth.AuthInfoResponse;
import com.maximilian.practice.rest.exception.GeneralException;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;

public interface AuthInfoResolver {

    default AuthInfoResponse getCurrentUser() {
        AuthInfoResponse authInfo = (AuthInfoResponse) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        if(authInfo == null) {
            throw new GeneralException("Could not identify current user.", HttpStatus.UNAUTHORIZED);
        }
        return authInfo;
    }

}
