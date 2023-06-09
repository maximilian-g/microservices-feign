package com.maximilian.practice.service;

import com.maximilian.practice.domain.auth.AuthRequest;
import com.maximilian.practice.domain.auth.Role;
import com.maximilian.practice.model.RolesToUserLink;
import com.maximilian.practice.model.RolesToUserLinkId;
import com.maximilian.practice.model.UserCredentials;
import com.maximilian.practice.model.UserRole;
import com.maximilian.practice.repository.RolesToUserLinkRepository;
import com.maximilian.practice.repository.UserDetailsRepository;
import com.maximilian.practice.rest.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDetailsRepository userRepository;
    private final RolesToUserLinkRepository linkRepository;
    private final PasswordEncoder encoder;
    private final RolesService rolesService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return findByUsername(username);
    }

    public UserCredentials findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with nickname '" + username + "' does not exist."));
    }

    public UserCredentials register(AuthRequest request) {
        log.info("Registering user {}", request);
        if (userRepository.existsByUsername(request.username())) {
            throw new GeneralException("User already exists");
        }
        UserCredentials userCredentials = new UserCredentials();
        userCredentials.setUsername(request.username());

        UserRole roleByEnum = rolesService.getRoleByEnum(Role.USER);
        RolesToUserLinkId linkId = new RolesToUserLinkId();

        userCredentials.setPassword(encoder.encode(request.password()));
        userCredentials = userRepository.save(userCredentials);

        linkId.setRoleId(roleByEnum.getId());
        linkId.setUserId(userCredentials.getId());

        RolesToUserLink link = new RolesToUserLink();
        link.setId(linkId);
        link.setRole(roleByEnum);
        link.setHolder(userCredentials);
        link = linkRepository.save(link);

        userCredentials.setRoles(List.of(link));
        return userCredentials;
    }

}
