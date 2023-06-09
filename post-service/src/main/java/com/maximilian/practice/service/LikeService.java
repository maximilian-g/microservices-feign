package com.maximilian.practice.service;

import com.maximilian.practice.model.Like;
import com.maximilian.practice.model.LikeId;
import com.maximilian.practice.repository.LikeRepository;
import com.maximilian.practice.rest.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    public Like addLike(Long postId, Long userId) {
        LikeId likeId = new LikeId();
        likeId.setPostId(postId);
        likeId.setUserId(userId);

        if(likeRepository.existsById(likeId)) {
            log.warn("User " + userId + " already liked post " + postId);
            throw new GeneralException("User " + userId + " already liked post " + postId);
        }

        Like like = new Like();
        like.setId(likeId);
        like.setCreatedAt(Instant.now());
        like.setTimezone(ZoneId.systemDefault().getId());

        return likeRepository.saveAndFlush(like);
    }

    public void removeLike(Long postId, Long userId) {
        LikeId likeId = new LikeId();
        likeId.setPostId(postId);
        likeId.setUserId(userId);
        likeRepository.deleteById(likeId);
    }

    public long getLikesQuantityOfPost(Long postId) {
        return likeRepository.countLikeByLikedPostId(postId);
    }

    public List<Like> getLikesOfPost(Long postId) {
        return likeRepository.findLikesByLikedPostId(postId);
    }

}
