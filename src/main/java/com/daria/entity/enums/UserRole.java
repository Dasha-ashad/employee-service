package com.daria.entity.enums;

import lombok.Getter;

@Getter
public enum UserRole {
    ADMIN("ROLE_ADMIN"), EMPLOYEE("ROLE_EMPLOYEE"), HEAD("ROLE_HEAD");

    private final String roleName;
    UserRole(String headRole) {
        this.roleName = headRole;
    }
}
