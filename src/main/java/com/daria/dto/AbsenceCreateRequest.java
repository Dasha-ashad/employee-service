package com.daria.dto;

import com.daria.entity.enums.AbsenceStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record AbsenceCreateRequest(
    @NotNull(message = "Employee ID is required")
    Long employeeId,
    @NotNull(message = "Start date is required")
    LocalDate startDate,
    LocalDate endDate,
    String description,
    AbsenceStatus status
) {}

