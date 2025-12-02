package com.daria.dto;

import com.daria.entity.enums.CompetenceRank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record TrainingUpdateRequest(
    @Size(max = 150, message = "Training name must not exceed 150 characters")
    String trainingName,
    LocalDate startDate,
    LocalDate endDate,
    CompetenceRank levelBefore,
    CompetenceRank levelAfter
) {}

