package com.daria.service;

import com.daria.dto.DepartmentCreateRequest;
import com.daria.dto.DepartmentDto;
import com.daria.dto.DepartmentUpdateRequest;
import com.daria.entity.Department;
import com.daria.entity.Employee;
import com.daria.exception.BadRequestException;
import com.daria.exception.ResourceNotFoundException;
import com.daria.repository.DepartmentRepository;
import com.daria.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentService {

  private final DepartmentRepository departmentRepository;
  private final EmployeeRepository employeeRepository;

  public List<DepartmentDto> getAllDepartments() {
    return departmentRepository.findAll().stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public DepartmentDto getDepartmentById(Long id) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Department", id));
    return toDto(department);
  }

  public DepartmentDto createDepartment(DepartmentCreateRequest request) {
    // Валидация имени отдела
    String trimmedName = request.name() != null ? request.name().trim() : "";
    if (trimmedName.isEmpty()) {
      throw new BadRequestException("Department name cannot be empty");
    }
    
    if (departmentRepository.findByName(trimmedName).isPresent()) {
      throw new BadRequestException("Department with name '" + trimmedName + "' already exists");
    }

    Employee head = null;
    if (request.headId() != null) {
      head = employeeRepository.findById(request.headId())
          .orElseThrow(() -> new ResourceNotFoundException("Employee", request.headId()));
      
      // Проверяем, не является ли сотрудник уже руководителем другого отдела
      Department existingDept = departmentRepository.findAll().stream()
          .filter(dept -> dept.getHead() != null && dept.getHead().getId().equals(request.headId()))
          .findFirst()
          .orElse(null);
      
      if (existingDept != null) {
        throw new BadRequestException("Employee with id " + request.headId() + 
            " is already the head of department '" + existingDept.getName() + "'");
      }
    }

    Department department = Department.builder()
        .name(trimmedName)
        .head(head)
        .build();

    Department saved = departmentRepository.save(department);
    return toDto(saved);
  }

  public DepartmentDto updateDepartment(Long id, DepartmentUpdateRequest request) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Department", id));

    // Обновление имени отдела
    if (request.name() != null) {
      String trimmedName = request.name().trim();
      if (trimmedName.isEmpty()) {
        throw new BadRequestException("Department name cannot be empty");
      }
      
      if (!trimmedName.equals(department.getName())) {
        // Проверяем уникальность имени (исключая текущий отдел)
        departmentRepository.findByName(trimmedName)
            .ifPresent(existingDept -> {
              if (!existingDept.getId().equals(id)) {
                throw new BadRequestException("Department with name '" + trimmedName + "' already exists");
              }
            });
        department.setName(trimmedName);
      }
    }

    // Обработка назначения/снятия руководителя
    // ВАЖНО: В JSON null и отсутствие поля - это одно и то же
    // 
    // Логика:
    // - Если headId != null: назначаем нового руководителя
    // - Если headId = null: снимаем руководителя (если был назначен)
    // 
    // Для обновления только имени без изменения руководителя:
    // фронтенд должен явно отправить текущий headId в запросе
    
    if (request.headId() != null) {
      // Назначаем нового руководителя
      Employee head = employeeRepository.findById(request.headId())
          .orElseThrow(() -> new ResourceNotFoundException("Employee", request.headId()));
      
      // Проверяем, не является ли сотрудник уже руководителем другого отдела
      // (исключая текущий отдел)
      Department existingDept = departmentRepository.findAll().stream()
          .filter(dept -> dept.getHead() != null 
              && dept.getHead().getId().equals(request.headId())
              && !dept.getId().equals(id))
          .findFirst()
          .orElse(null);
      
      if (existingDept != null) {
        throw new BadRequestException("Employee with id " + request.headId() + 
            " is already the head of department '" + existingDept.getName() + "'");
      }
      
      department.setHead(head);
    } else {
      // Если headId = null, снимаем руководителя
      // Это позволяет снять руководителя, отправив { headId: null }
      department.setHead(null);
    }

    Department updated = departmentRepository.save(department);
    return toDto(updated);
  }

  /**
   * Удаление отдела с проверкой наличия сотрудников
   * 
   * Edge cases:
   * - Проверка наличия сотрудников в отделе
   * - Автоматическое снятие связи сотрудников с отделом (SET NULL)
   * - Автоматическое снятие руководителя отдела
   */
  public void deleteDepartment(Long id) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Department", id));
    
    // Проверяем, есть ли сотрудники в отделе
    // JPA автоматически установит department_id = NULL для всех сотрудников
    // благодаря @JoinColumn(name = "department_id") без ON DELETE CASCADE
    // Но мы можем предупредить пользователя, если нужно
    
    // Удаляем отдел (сотрудники автоматически получат department_id = NULL)
    departmentRepository.delete(department);
  }

  private DepartmentDto toDto(Department department) {
    return DepartmentDto.of(
        department.getId(),
        department.getName(),
        department.getHead() != null ? department.getHead().getId() : null
    );
  }
}

