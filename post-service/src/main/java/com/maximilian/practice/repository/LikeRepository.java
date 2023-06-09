package com.maximilian.practice.repository;

import com.maximilian.practice.model.Like;
import com.maximilian.practice.model.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, LikeId> {

    long countLikeByLikedPostId(Long id);

    List<Like> findLikesByLikedPostId(Long id);

}
