package com.maximilian.practice.model;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users_users")
@Data
public class UserToUserLink {

    @EmbeddedId
    private UserToUserLinkId id;

    @ManyToOne
    @JoinColumn(name = "from_id", updatable = false, insertable = false)
    private User follower;

    @ManyToOne
    @JoinColumn(name = "to_id", updatable = false, insertable = false)
    private User contentCreator;

}
