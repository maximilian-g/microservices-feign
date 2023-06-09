package com.maximilian.practice.domain.auth;

import java.util.HashMap;
import java.util.Map;

public enum Role {
    ADMIN,
    SYSTEM,
    USER;

    private static final Map<String, Role> nameToRoleMap = new HashMap<>();

    static {
        for (Role role : Role.values()) {
            nameToRoleMap.put(role.name(), role);
        }
    }

    public static Role getByName(String name) {
        return nameToRoleMap.get(name);
    }

}
