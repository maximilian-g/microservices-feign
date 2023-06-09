package com.maximilian.practice.repository;

import com.maximilian.practice.model.UserToUserLink;
import com.maximilian.practice.model.UserToUserLinkId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserToUserLinkRepository extends JpaRepository<UserToUserLink, UserToUserLinkId> {
}
