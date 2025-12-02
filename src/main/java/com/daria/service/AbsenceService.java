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

  public AbsenceDto createAbsence(AbsenceCreateRequest request) {
    Employee employee = employeeRepository.findById(request.employeeId())
        .orElseThrow(() -> new ResourceNotFoundException("Employee", request.employeeId()));

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

  public AbsenceDto updateAbsence(Long id, AbsenceUpdateRequest request) {
    AbsenceEntity absence = absenceRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Absence", id));

    if (request.startDate() != null) {
      absence.setStartDate(request.startDate());
    }
    if (request.endDate() != null) {
      absence.setEndDate(request.endDate());
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

