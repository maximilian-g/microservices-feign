package com.maximilian.practice.repository;

import com.maximilian.practice.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findPostsByUserId(Long userId);

    List<Post> findPostsByParentPostId(Long postId);

    List<Post> findByParentPostIdIsNotNull();

}
