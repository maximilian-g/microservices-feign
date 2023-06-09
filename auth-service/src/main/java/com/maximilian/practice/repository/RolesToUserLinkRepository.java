package com.maximilian.practice.repository;

import com.maximilian.practice.model.RolesToUserLink;
import com.maximilian.practice.model.RolesToUserLinkId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolesToUserLinkRepository extends JpaRepository<RolesToUserLink, RolesToUserLinkId> {
}
