package com.maximilian.practice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class UserToUserLinkId implements Serializable {

    @Column(name = "from_id")
    private Long fromId;
    @Column(name = "to_id")
    private Long toId;

}
