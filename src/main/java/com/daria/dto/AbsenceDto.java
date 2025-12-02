package com.daria.dto;

import com.daria.entity.enums.AbsenceStatus;

import java.time.LocalDate;

public record AbsenceDto(
    Long id,
    Long employeeId,
    LocalDate startDate,
    LocalDate endDate,
    String description,
    AbsenceStatus status
) {
  public static AbsenceDto of(Long id, Long employeeId, LocalDate startDate,
                              LocalDate endDate, String description, AbsenceStatus status) {
    return new AbsenceDto(id, employeeId, startDate, endDate, description, status);
  }
}

