package com.maximilian.practice.domain.post;

import java.time.ZonedDateTime;

public record PostResponse(Long id, String content, Long userId,
                           ZonedDateTime createdAt, ZonedDateTime updatedAt,
                           Long associatedPostId) {
}
