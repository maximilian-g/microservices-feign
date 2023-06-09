package com.maximilian.practice.startup;

import com.maximilian.practice.domain.auth.Role;
import com.maximilian.practice.service.RolesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Slf4j
@RequiredArgsConstructor
public class OnStartAction {

    private final RolesService rolesService;

    @PostConstruct
    private void init() {
        for (Role role : Role.values()) {
            if (!rolesService.roleExists(role)) {
                log.info("Creating role " + role);
                log.info("Created: " + rolesService.createRole(role));
            }
        }
    }

}
