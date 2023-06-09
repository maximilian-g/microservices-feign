package com.maximilian.practice.controller;

import com.maximilian.practice.domain.post.LikeResponse;
import com.maximilian.practice.domain.post.PostRequest;
import com.maximilian.practice.domain.post.PostResponse;
import com.maximilian.practice.rest.RestResponse;
import com.maximilian.practice.service.PostAndLikeFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@Validated
public class PostController {

    private final PostAndLikeFacade postAndLikeFacade;

    @PostMapping
    public PostResponse createPost(@RequestBody @Valid PostRequest postRequest) {
        return postAndLikeFacade.createPost(postRequest);
    }

    // post is created as a comment
    @PostMapping("/{id}")
    public PostResponse createComment(@PathVariable Long id,
                                      @RequestBody @Valid PostRequest postRequest) {
        return postAndLikeFacade.createComment(postRequest, id);
    }

    @PostMapping("/{id}/like")
    public RestResponse addLike(@PathVariable Long id) {
        return postAndLikeFacade.addLike(id);
    }

    @GetMapping("/{id}/likes/quantity")
    public long getLikesQuantity(@PathVariable Long id) {
        return postAndLikeFacade.getLikesQuantityForPost(id);
    }

    @GetMapping("/{id}/likes")
    public List<LikeResponse> getLikes(@PathVariable Long id) {
        return postAndLikeFacade.getLikesForPost(id);
    }

    @PostMapping("/{id}/unlike")
    public RestResponse removeLike(@PathVariable Long id) {
        return postAndLikeFacade.removeLike(id);
    }

    @GetMapping("/{id}")
    public PostResponse getPostById(@PathVariable Long id) {
        return postAndLikeFacade.getPostById(id);
    }

    @GetMapping("/{id}/comments")
    public List<PostResponse> getComments(@PathVariable Long id) {
        return postAndLikeFacade.getCommentsOfPost(id);
    }

    @GetMapping
    public List<PostResponse> getPostsByUserId(@RequestParam Long userId) {
        return postAndLikeFacade.getPostsByUserId(userId);
    }

    @DeleteMapping("/{id}")
    public RestResponse deletePost(@PathVariable Long id) {
        return postAndLikeFacade.deletePost(id);
    }

}
