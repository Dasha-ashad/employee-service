package com.daria.dto;

import com.daria.entity.enums.AbsenceStatus;

import java.time.LocalDate;

/**
 * DTO для пропуска сотрудника
 * 
 * Содержит информацию о пропуске:
 * - id - идентификатор пропуска
 * - employeeId - идентификатор сотрудника
 * - employeeName - ФИО сотрудника (для удобства отображения)
 * - startDate - дата начала пропуска
 * - endDate - дата окончания пропуска (может быть null для однодневных пропусков)
 * - description - описание пропуска
 * - status - статус пропуска (GOOD_REASON - уважительная, BAD_REASON - неуважительная)
 */
public record AbsenceDto(
    Long id,
    Long employeeId,
    String employeeName, // Добавляем имя сотрудника для удобства
    LocalDate startDate,
    LocalDate endDate,
    String description,
    AbsenceStatus status
) {
  public static AbsenceDto of(Long id, Long employeeId, LocalDate startDate,
                              LocalDate endDate, String description, AbsenceStatus status) {
    return new AbsenceDto(id, employeeId, null, startDate, endDate, description, status);
  }
  
  public static AbsenceDto of(Long id, Long employeeId, String employeeName, LocalDate startDate,
                              LocalDate endDate, String description, AbsenceStatus status) {
    return new AbsenceDto(id, employeeId, employeeName, startDate, endDate, description, status);
  }
}

