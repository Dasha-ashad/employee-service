package com.daria.service;

import com.daria.dto.AbsenceCreateRequest;
import com.daria.dto.AbsenceDto;
import com.daria.dto.AbsenceUpdateRequest;
import com.daria.entity.AbsenceEntity;
import com.daria.entity.Employee;
import com.daria.entity.enums.AbsenceStatus;
import com.daria.exception.ResourceNotFoundException;
import com.daria.repository.AbsenceRepository;
import com.daria.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class AbsenceService {

  private final AbsenceRepository absenceRepository;
  private final EmployeeRepository employeeRepository;

  public List<AbsenceDto> getAllAbsences() {
    return absenceRepository.findAll().stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public AbsenceDto getAbsenceById(Long id) {
    AbsenceEntity absence = absenceRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Absence", id));
    return toDto(absence);
  }

  public List<AbsenceDto> getAbsencesByEmployeeId(Long employeeId) {
    return absenceRepository.findByEmployeeId(employeeId).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  /**
   * Создание пропуска с валидацией данных
   * 
   * Валидация:
   * - employeeId должен существовать
   * - endDate должна быть после startDate
   */
  public AbsenceDto createAbsence(AbsenceCreateRequest request) {
    Employee employee = employeeRepository.findById(request.employeeId())
        .orElseThrow(() -> new ResourceNotFoundException("Employee", request.employeeId()));

    // Валидация дат: endDate должна быть после startDate
    if (request.startDate() != null && request.endDate() != null) {
      if (request.endDate().isBefore(request.startDate())) {
        throw new com.daria.exception.BadRequestException("End date cannot be before start date");
      }
    }

    AbsenceEntity absence = AbsenceEntity.builder()
        .employee(employee)
        .startDate(request.startDate())
        .endDate(request.endDate())
        .description(request.description())
        .status(request.status() != null ? request.status() : AbsenceStatus.GOOD_REASON)
        .build();

    AbsenceEntity saved = absenceRepository.save(absence);
    return toDto(saved);
  }

  /**
   * Обновление пропуска с валидацией данных
   */
  public AbsenceDto updateAbsence(Long id, AbsenceUpdateRequest request) {
    AbsenceEntity absence = absenceRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Absence", id));

    java.time.LocalDate startDate = request.startDate() != null ? request.startDate() : absence.getStartDate();
    java.time.LocalDate endDate = request.endDate() != null ? request.endDate() : absence.getEndDate();

    if (request.startDate() != null) {
      absence.setStartDate(request.startDate());
    }
    if (request.endDate() != null) {
      absence.setEndDate(request.endDate());
    }
    
    // Валидация дат после обновления
    if (startDate != null && endDate != null && endDate.isBefore(startDate)) {
      throw new com.daria.exception.BadRequestException("End date cannot be before start date");
    }
    
    if (request.description() != null) {
      absence.setDescription(request.description());
    }
    if (request.status() != null) {
      absence.setStatus(request.status());
    }

    AbsenceEntity updated = absenceRepository.save(absence);
    return toDto(updated);
  }

  public void deleteAbsence(Long id) {
    AbsenceEntity absence = absenceRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Absence", id));
    absenceRepository.delete(absence);
  }

  private AbsenceDto toDto(AbsenceEntity absence) {
    return AbsenceDto.of(
        absence.getId(),
        absence.getEmployee().getId(),
        absence.getStartDate(),
        absence.getEndDate(),
        absence.getDescription(),
        absence.getStatus()
    );
  }
}

