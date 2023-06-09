package com.maximilian.practice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class LikeId implements Serializable {

    @Column(name = "user_id")
    private Long userId;
    @Column(name = "post_id")
    private Long postId;

}
