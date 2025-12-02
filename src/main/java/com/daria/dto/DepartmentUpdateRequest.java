package com.daria.dto;

import jakarta.validation.constraints.Size;

public record DepartmentUpdateRequest(
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    String name,
    Long headId
) {}

