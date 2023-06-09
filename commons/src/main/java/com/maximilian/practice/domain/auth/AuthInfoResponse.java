package com.maximilian.practice.domain.auth;

import java.util.List;

public record AuthInfoResponse(Long userId, String username, List<Role> roles) {
}
