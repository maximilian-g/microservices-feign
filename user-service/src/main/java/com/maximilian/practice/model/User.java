package com.maximilian.practice.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
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
@Table(name = "users")
@Data
public class User {

    @Id
    private Long id;

    // not possible to change or update after creating user
    @Column(nullable = false, unique = true)
    @NotNull(message = "Nickname must not be blank.")
    @Size(min = 3, max = 45, message = "Nickname must contain more than 3 and less than 45 characters.")
    private String nickname;
    // possible to change(update) this field
    @Column(nullable = false)
    @NotNull(message = "Display name must not be blank.")
    @Size(min = 3, max = 45, message = "Display name must contain more than 3 and less than 45 characters.")
    private String displayName;

    @Column
    @Size(max = 255, message = "Bio must contain maximum 255 characters.")
    private String bio;
    @Column(nullable = false)
    @NotNull
    private Instant createdAt;
    @Column(nullable = false)
    @NotNull
    private Instant updatedAt;
    @Column(nullable = false)
    @NotNull
    private String timezone;

    @OneToMany(mappedBy = "follower", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<UserToUserLink> following = new ArrayList<>();

    @OneToMany(mappedBy = "contentCreator", fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Collection<UserToUserLink> followers = new ArrayList<>();

    @Transient
    private ZoneId zoneId;

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
