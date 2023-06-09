package com.maximilian.practice.mapper;

import com.maximilian.practice.domain.post.PostResponse;
import com.maximilian.practice.model.Post;

import java.time.ZonedDateTime;

public class PostResponseMapper {

    private PostResponseMapper() {
    }

    public static PostResponse map(Post post) {
        return new PostResponse(post.getId(), post.getContent(), post.getUserId(),
                ZonedDateTime.ofInstant(post.getCreatedAt(), post.getZoneId()),
                ZonedDateTime.ofInstant(post.getUpdatedAt(), post.getZoneId()),
                post.getParentPost() == null ? null : post.getParentPost().getId());
    }

}
