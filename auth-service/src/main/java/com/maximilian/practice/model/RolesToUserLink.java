package com.maximilian.practice.model;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "users_roles")
@Data
public class RolesToUserLink {

    @EmbeddedId
    private RolesToUserLinkId id;

    @ManyToOne
    @JoinColumn(name = "user_id", updatable = false, insertable = false)
    private UserCredentials holder;

    @ManyToOne
    @JoinColumn(name = "role_id", updatable = false, insertable = false)
    private UserRole role;

}
