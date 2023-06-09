package com.maximilian.practice.service;

import com.maximilian.practice.domain.auth.Role;
import com.maximilian.practice.model.UserRole;
import com.maximilian.practice.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RolesService {

    private final RoleRepository roleRepository;

    public UserRole createRole(Role role) {
        UserRole newRole = new UserRole();
        newRole.setRole(role);
        return roleRepository.saveAndFlush(newRole);
    }

    public boolean roleExists(Role role) {
        return roleRepository.existsByRole(role);
    }

    public UserRole getRoleByEnum(Role role) {
        return roleRepository.getByRole(role);
    }

}
