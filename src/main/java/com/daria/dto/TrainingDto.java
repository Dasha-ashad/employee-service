package com.daria.dto;

import com.daria.entity.enums.CompetenceRank;

import java.time.LocalDate;

public record TrainingDto(
    Long id,
    Long employeeId,
    String trainingName,
    LocalDate startDate,
    LocalDate endDate,
    CompetenceRank levelBefore,
    CompetenceRank levelAfter
) {
  public static TrainingDto of(Long id, Long employeeId, String trainingName,
                               LocalDate startDate, LocalDate endDate,
                               CompetenceRank levelBefore, CompetenceRank levelAfter) {
    return new TrainingDto(id, employeeId, trainingName, startDate, endDate, levelBefore, levelAfter);
  }
}

