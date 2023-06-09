package com.maximilian.practice.mapper;

import com.maximilian.practice.domain.user.UserResponse;
import com.maximilian.practice.model.User;

import java.time.ZonedDateTime;

public class UserResponseMapper {

    private UserResponseMapper() {
    }

    public static UserResponse map(User user) {
        return new UserResponse(user.getId(),
                user.getNickname(),
                user.getDisplayName(),
                user.getBio(),
                ZonedDateTime.ofInstant(user.getCreatedAt(), user.getZoneId()),
                ZonedDateTime.ofInstant(user.getUpdatedAt(), user.getZoneId()));
    }

}
