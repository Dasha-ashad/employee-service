package com.daria.dto;

import com.daria.entity.enums.CompetenceRank;
import com.daria.entity.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EmployeeDto(
    Long id,
    Long userId,
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    String fullName,
    @NotNull(message = "Gender is required")
    Gender gender,
    LocalDate birthDate,
    @NotNull(message = "Hire date is required")
    LocalDate hireDate,
    LocalDate fireDate,
    @NotNull(message = "Competence rank is required")
    CompetenceRank competenceRank,
    @Positive(message = "Competence level must be positive")
    Integer competenceLevel,
    Long departmentId
) {
  public static EmployeeDto of(Long id, Long userId, String fullName, Gender gender,
                               LocalDate birthDate, LocalDate hireDate, LocalDate fireDate,
                               CompetenceRank competenceRank, Integer competenceLevel,
                               Long departmentId) {
    return new EmployeeDto(id, userId, fullName, gender, birthDate, hireDate, fireDate,
        competenceRank, competenceLevel, departmentId);
  }
}

