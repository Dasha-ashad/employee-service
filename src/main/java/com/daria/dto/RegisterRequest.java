package com.daria.dto;

import com.daria.entity.enums.UserRole;

public record RegisterRequest(
    String login,
    String password,
    UserRole role
) {}