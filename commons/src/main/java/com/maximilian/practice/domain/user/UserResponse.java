package com.maximilian.practice.domain.user;

import java.time.ZonedDateTime;

public record UserResponse(Long id, String nickname, String displayName, String bio,
                           ZonedDateTime createdAt, ZonedDateTime updatedAt) {
}
