package com.daria.dto;

import com.daria.entity.enums.CompetenceRank;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TrainingCreateRequest(
    @NotNull(message = "Employee ID is required")
    Long employeeId,
    @NotBlank(message = "Training name is required")
    @Size(max = 150, message = "Training name must not exceed 150 characters")
    String trainingName,
    LocalDate startDate,
    LocalDate endDate,
    CompetenceRank levelBefore,
    CompetenceRank levelAfter
) {}

