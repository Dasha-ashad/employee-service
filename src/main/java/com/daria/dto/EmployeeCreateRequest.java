package com.daria.dto;

import com.daria.entity.enums.CompetenceRank;
import com.daria.entity.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EmployeeCreateRequest(
    Long userId,
    @NotBlank(message = "Full name is required")
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    String fullName,
    @NotNull(message = "Gender is required")
    Gender gender,
    LocalDate birthDate,
    @NotNull(message = "Hire date is required")
    LocalDate hireDate,
    @NotNull(message = "Competence rank is required")
    CompetenceRank competenceRank,
    @Positive(message = "Competence level must be between 1 and 3")
    Integer competenceLevel,
    Long departmentId
) {}

