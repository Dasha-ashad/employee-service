package com.daria.dto;

import com.daria.entity.enums.AbsenceStatus;

import java.time.LocalDate;

public record AbsenceUpdateRequest(
    LocalDate startDate,
    LocalDate endDate,
    String description,
    AbsenceStatus status
) {}

