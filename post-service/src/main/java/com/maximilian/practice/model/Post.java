package com.maximilian.practice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "posts")
@Data
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Size(min = 3, max = 255, message = "Post length must between 3 and 255.")
    private String content;
    @Column(nullable = false)
    @NotNull
    private Long userId;
    @Column(nullable = false)
    @NotNull
    private Instant createdAt;
    @Column(nullable = false)
    @NotNull
    private Instant updatedAt;
    @Column(nullable = false)
    @NotNull
    private String timezone;

    @Transient
    private ZoneId zoneId;

    // comments
    @OneToMany(mappedBy = "parentPost", fetch = FetchType.LAZY, orphanRemoval = true)
    private Collection<Post> childPosts = new ArrayList<>();

    // parent_post_id will be created as field in "posts" table
    @ManyToOne(fetch = FetchType.LAZY)
    private Post parentPost;

    @PreUpdate
    @PrePersist
    public void preUpdate() {
        updatedAt = Instant.now();
        zoneId = ZoneId.of(timezone);
    }

    @PostLoad
    public void postLoad() {
        zoneId = ZoneId.of(timezone);
    }

}
