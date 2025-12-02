package com.daria.dto;

import com.daria.entity.enums.CompetenceRank;
import com.daria.entity.enums.Gender;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record EmployeeUpdateRequest(
    @Size(max = 100, message = "Full name must not exceed 100 characters")
    String fullName,
    Gender gender,
    LocalDate birthDate,
    LocalDate hireDate,
    LocalDate fireDate,
    CompetenceRank competenceRank,
    Integer competenceLevel,
    Long departmentId
) {}

