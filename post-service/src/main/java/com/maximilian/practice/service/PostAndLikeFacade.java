package com.maximilian.practice.service;

import com.maximilian.practice.domain.auth.AuthInfoResponse;
import com.maximilian.practice.domain.post.LikeResponse;
import com.maximilian.practice.domain.post.PostRequest;
import com.maximilian.practice.domain.post.PostResponse;
import com.maximilian.practice.mapper.PostResponseMapper;
import com.maximilian.practice.model.Post;
import com.maximilian.practice.rest.RestResponse;
import com.maximilian.practice.rest.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class PostAndLikeFacade implements AuthInfoResolver {

    private final PostService postService;
    private final LikeService likeService;

    public PostResponse createPost(PostRequest postRequest) {
        AuthInfoResponse authInfo = getCurrentUser();
        log.info("User '" + authInfo.userId() + "' is trying to create post for user '" + postRequest.userId() + "'.");
        if (!postRequest.userId().equals(authInfo.userId())) {
            throw new GeneralException("Cannot create post.", HttpStatus.UNAUTHORIZED);
        }
        return PostResponseMapper.map(postService.createPost(
                postRequest.content(), postRequest.userId()
        ));
    }

    public PostResponse createComment(PostRequest postRequest, Long associatedPostId) {
        AuthInfoResponse authInfo = getCurrentUser();
        log.info("User '" + authInfo.userId() + "' is trying to create comment for user '" + postRequest.userId() + "'.");
        if (!postRequest.userId().equals(authInfo.userId())) {
            throw new GeneralException("Cannot create comment.", HttpStatus.UNAUTHORIZED);
        }
        return PostResponseMapper.map(postService.createComment(postRequest, associatedPostId));
    }


    public PostResponse getPostById(Long postId) {
        return PostResponseMapper.map(postService.getPostById(postId));
    }

    public List<PostResponse> getCommentsOfPost(Long postId) {
        return postService.getCommentsOfPost(postId).stream()
                .map(PostResponseMapper::map)
                .collect(Collectors.toList());
    }

    public List<PostResponse> getPostsByUserId(Long userId) {
        return postService.getPostsByUserId(userId).stream()
                .map(PostResponseMapper::map)
                .collect(Collectors.toList());
    }

    // deletes a post and all comments of this post
    public RestResponse deletePost(Long id) {
        AuthInfoResponse authInfo = getCurrentUser();
        Post post = postService.getPostById(id);
        log.info("User '" + authInfo.userId() + "' is trying to delete post for user '" + post.getUserId() + "'.");
        if (!post.getUserId().equals(authInfo.userId())) {
            throw new GeneralException("Cannot create comment.", HttpStatus.UNAUTHORIZED);
        }
        RestResponse response = new RestResponse("Successfully deleted post and " +
                post.getChildPosts().size() + " comments.");
        post.getChildPosts().forEach(childPost ->
                deleteCommentLikes(childPost.getId()));
        likeService.removeLikes(id);
        postService.deletePostById(id);
        return response;
    }

    protected void deleteCommentLikes(Long commentId) {
        Post post = postService.getPostById(commentId);
        log.info("Removing likes of comment #" +
                post.getId() + " because parent post is being removed...");
        post.getChildPosts().forEach(childPost -> {
            likeService.removeLikes(childPost.getId());
        });
        likeService.removeLikes(commentId);
    }

    public RestResponse addLike(Long postId) {
        AuthInfoResponse authInfo = getCurrentUser();
        log.info("User '" + authInfo.userId() + "' is trying to like post '" + postId + "'.");
        postService.checkIfExistsOrThrow(postId);
        likeService.addLike(postId, authInfo.userId());
        return new RestResponse("Successfully liked post " + postId + ".");
    }

    public RestResponse removeLike(Long postId) {
        AuthInfoResponse authInfo = getCurrentUser();
        log.info("User '" + authInfo.userId() + "' is trying to remove like from post '" + postId + "'.");
        likeService.removeLike(postId, authInfo.userId());
        return new RestResponse("Successfully removed like from post " + postId + ".");
    }

    public long getLikesQuantityForPost(Long postId) {
        return likeService.getLikesQuantityOfPost(postId);
    }

    public List<LikeResponse> getLikesForPost(Long postId) {
        return likeService.getLikesOfPost(postId).stream().map(l ->
                new LikeResponse(l.getId().getPostId(), l.getLikedPost().getUserId())
        ).collect(Collectors.toList());
    }

}
