package com.maximilian.practice.repository;

import com.maximilian.practice.domain.auth.Role;
import com.maximilian.practice.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<UserRole, Long> {

    boolean existsByRole(Role role);

    UserRole getByRole(Role role);

}
