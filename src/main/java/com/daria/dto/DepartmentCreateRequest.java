package com.daria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DepartmentCreateRequest(
    @NotBlank(message = "Department name is required")
    @Size(max = 100, message = "Department name must not exceed 100 characters")
    String name,
    Long headId
) {}

