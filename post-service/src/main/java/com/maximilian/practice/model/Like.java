package com.maximilian.practice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.time.ZoneId;

@Entity
@Table(name = "likes")
@Data
public class Like {

    @EmbeddedId
    private LikeId id;

    @Column(nullable = false)
    @NotNull
    private Instant createdAt;
    @Column(nullable = false)
    @NotNull
    private String timezone;

    @ManyToOne
    @JoinColumn(name = "post_id", updatable = false, insertable = false)
    private Post likedPost;

    @Transient
    private ZoneId zoneId;

    @PostLoad
    public void postLoad() {
        zoneId = ZoneId.of(timezone);
    }

}
