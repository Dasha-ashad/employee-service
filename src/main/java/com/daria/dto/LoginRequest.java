package com.daria.dto;

public record LoginRequest(
    String username,
    String password
) {}