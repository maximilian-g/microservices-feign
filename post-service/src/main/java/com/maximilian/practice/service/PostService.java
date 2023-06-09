package com.maximilian.practice.service;

import com.maximilian.practice.domain.post.PostRequest;
import com.maximilian.practice.domain.user.UserResponse;
import com.maximilian.practice.model.Post;
import com.maximilian.practice.repository.PostRepository;
import com.maximilian.practice.rest.clients.UserServiceClient;
import com.maximilian.practice.rest.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserServiceClient userServiceClient;

    public Post createPost(String postContent, Long userId) {
        log.info("Creating post for user with id '" + userId + "'");
        UserResponse response = userServiceClient.getById(userId);
        log.info("Found user '" + response.nickname() + "', creating post...");
        Post post = new Post();
        post.setUserId(userId);
        post.setContent(postContent);
        post.setCreatedAt(Instant.now());
        post.setTimezone(ZoneId.systemDefault().getId());
        return postRepository.saveAndFlush(post);
    }

    public List<Post> getPostsByUserId(Long userId) {
        log.info("Getting posts for user with id '" + userId + "'...");
        return postRepository.findPostsByUserId(userId);
    }

    public Post getPostById(Long id) {
        log.info("Getting post with id '" + id + "'");
        return postRepository.findById(id)
                .orElseThrow(() ->
                        new GeneralException("Post with id '" + id + "' not found",
                                HttpStatus.NOT_FOUND)
                );
    }

    public Post createComment(PostRequest request, Long associatedPostId) {
        log.info("Creating comment from request " + request);
        Post associatedPost = getPostById(associatedPostId);
        Post comment = new Post();
        comment.setContent(request.content());
        comment.setCreatedAt(Instant.now());
        comment.setUserId(request.userId());
        comment.setParentPost(associatedPost);
        comment.setTimezone(ZoneId.systemDefault().getId());
        return postRepository.saveAndFlush(comment);
    }

    public List<Post> getAllComments() {
        log.info("Getting all comments (posts whose parent post is not null)");
        return postRepository.findByParentPostIdIsNotNull();
    }

    public List<Post> getCommentsOfPost(Long postId) {
        log.info("Getting all comments of post with id #" + postId);
        return postRepository.findPostsByParentPostId(postId);
    }

    public void deletePostById(Long id) {
        log.info("Deleting post by id #" + id);
        postRepository.deleteById(id);
    }

    public void checkIfExistsOrThrow(Long postId) {
        getPostById(postId);
    }
}
