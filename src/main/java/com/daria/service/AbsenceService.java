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

  /**
   * Получить все пропуски, отсортированные по дате начала (от новых к старым)
   */
  public List<AbsenceDto> getAllAbsences() {
    return absenceRepository.findAllOrderByStartDateDesc().stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public AbsenceDto getAbsenceById(Long id) {
    AbsenceEntity absence = absenceRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Absence", id));
    return toDto(absence);
  }

  /**
   * Получить пропуски сотрудника, отсортированные по дате начала
   */
  public List<AbsenceDto> getAbsencesByEmployeeId(Long employeeId) {
    return absenceRepository.findByEmployeeIdOrderByStartDateDesc(employeeId).stream()
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

    // Валидация дат
    if (request.startDate() == null) {
      throw new com.daria.exception.BadRequestException("Start date is required");
    }
    
    // Проверка на будущие даты (можно разрешить, но предупреждаем)
    if (request.startDate().isAfter(java.time.LocalDate.now().plusYears(1))) {
      throw new com.daria.exception.BadRequestException("Start date cannot be more than 1 year in the future");
    }
    
    // Валидация: endDate должна быть после startDate
    if (request.endDate() != null) {
      if (request.endDate().isBefore(request.startDate())) {
        throw new com.daria.exception.BadRequestException("End date cannot be before start date");
      }
    }
    
    // Проверка на пересекающиеся даты пропусков для того же сотрудника
    // (опционально - можно разрешить несколько пропусков одновременно)
    // Для строгой проверки можно раскомментировать:
    /*
    List<AbsenceEntity> existingAbsences = absenceRepository.findByEmployeeId(request.employeeId());
    for (AbsenceEntity existing : existingAbsences) {
      if (isDateRangeOverlapping(
          request.startDate(), 
          request.endDate() != null ? request.endDate() : request.startDate(),
          existing.getStartDate(),
          existing.getEndDate() != null ? existing.getEndDate() : existing.getStartDate())) {
        throw new com.daria.exception.BadRequestException(
            "Absence dates overlap with existing absence from " + existing.getStartDate() + 
            " to " + (existing.getEndDate() != null ? existing.getEndDate() : existing.getStartDate()));
      }
    }
    */

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
    
    // Проверка на будущие даты
    if (startDate != null && startDate.isAfter(java.time.LocalDate.now().plusYears(1))) {
      throw new com.daria.exception.BadRequestException("Start date cannot be more than 1 year in the future");
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

  /**
   * Проверка пересечения диапазонов дат
   * Используется для валидации пересекающихся пропусков (опционально)
   * 
   * @param start1 начало первого диапазона
   * @param end1 конец первого диапазона (может быть null - однодневный)
   * @param start2 начало второго диапазона
   * @param end2 конец второго диапазона (может быть null - однодневный)
   * @return true если диапазоны пересекаются
   */
  @SuppressWarnings("unused")
  private boolean isDateRangeOverlapping(
      java.time.LocalDate start1, 
      java.time.LocalDate end1,
      java.time.LocalDate start2, 
      java.time.LocalDate end2) {
    // Если endDate = null, считаем однодневным пропуском
    java.time.LocalDate actualEnd1 = end1 != null ? end1 : start1;
    java.time.LocalDate actualEnd2 = end2 != null ? end2 : start2;
    
    // Диапазоны пересекаются, если:
    // start1 <= end2 && start2 <= end1
    return !start1.isAfter(actualEnd2) && !start2.isAfter(actualEnd1);
  }

  private AbsenceDto toDto(AbsenceEntity absence) {
    // Получаем имя сотрудника для удобства отображения
    String employeeName = absence.getEmployee() != null 
        ? absence.getEmployee().getFullName() 
        : null;
    
    return AbsenceDto.of(
        absence.getId(),
        absence.getEmployee().getId(),
        employeeName,
        absence.getStartDate(),
        absence.getEndDate(),
        absence.getDescription(),
        absence.getStatus()
    );
  }
}

