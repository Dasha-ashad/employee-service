package com.daria.service;

import com.daria.dto.EmployeeCreateRequest;
import com.daria.dto.EmployeeDto;
import com.daria.dto.EmployeeUpdateRequest;
import com.daria.entity.Department;
import com.daria.entity.Employee;
import com.daria.exception.BadRequestException;
import com.daria.exception.ResourceNotFoundException;
import com.daria.repository.DepartmentRepository;
import com.daria.repository.EmployeeRepository;
import com.daria.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class EmployeeService {

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final UserRepository userRepository;

  public List<EmployeeDto> getAllEmployees() {
    return employeeRepository.findAll().stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public EmployeeDto getEmployeeById(Long id) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
    return toDto(employee);
  }

  public EmployeeDto createEmployee(EmployeeCreateRequest request) {
    if (request.userId() != null) {
      if (!userRepository.existsById(request.userId())) {
        throw new ResourceNotFoundException("User", request.userId());
      }
      
      if (employeeRepository.findAll().stream()
          .anyMatch(e -> e.getUser() != null && e.getUser().getId().equals(request.userId()))) {
        throw new BadRequestException("User is already associated with another employee");
      }
    }

    Department department = null;
    if (request.departmentId() != null) {
      department = departmentRepository.findById(request.departmentId())
          .orElseThrow(() -> new ResourceNotFoundException("Department", request.departmentId()));
    }

    if (request.competenceLevel() != null && 
        (request.competenceLevel() < 1 || request.competenceLevel() > 3)) {
      throw new BadRequestException("Competence level must be between 1 and 3");
    }

    // Валидация дат: hireDate должна быть после birthDate
    if (request.birthDate() != null && request.hireDate() != null) {
      if (request.hireDate().isBefore(request.birthDate())) {
        throw new BadRequestException("Hire date cannot be before birth date");
      }
    }

    // Валидация возраста: сотрудник должен быть старше 16 лет
    if (request.birthDate() != null && request.hireDate() != null) {
      long years = java.time.temporal.ChronoUnit.YEARS.between(request.birthDate(), request.hireDate());
      if (years < 16) {
        throw new BadRequestException("Employee must be at least 16 years old at hire date");
      }
    }

    Employee employee = Employee.builder()
        .user(request.userId() != null ? 
            userRepository.findById(request.userId()).orElse(null) : null)
        .fullName(request.fullName())
        .gender(request.gender())
        .birthDate(request.birthDate())
        .hireDate(request.hireDate())
        .competenceRank(request.competenceRank())
        .competenceLevel(request.competenceLevel())
        .department(department)
        .build();

    Employee saved = employeeRepository.save(employee);
    return toDto(saved);
  }

  /**
   * Обновление сотрудника с валидацией данных
   * 
   * Валидация:
   * - competenceLevel должен быть от 1 до 3
   * - hireDate должна быть после birthDate
   * - fireDate должна быть после hireDate (если указана)
   * - departmentId должен существовать (если указан)
   */
  public EmployeeDto updateEmployee(Long id, EmployeeUpdateRequest request) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Employee", id));

    // Обновление полей
    if (request.fullName() != null) {
      employee.setFullName(request.fullName());
    }
    if (request.gender() != null) {
      employee.setGender(request.gender());
    }
    
    // Валидация и обновление дат
    LocalDate birthDate = request.birthDate() != null ? request.birthDate() : employee.getBirthDate();
    LocalDate hireDate = request.hireDate() != null ? request.hireDate() : employee.getHireDate();
    LocalDate fireDate = request.fireDate() != null ? request.fireDate() : employee.getFireDate();
    
    if (request.birthDate() != null) {
      employee.setBirthDate(request.birthDate());
    }
    if (request.hireDate() != null) {
      employee.setHireDate(request.hireDate());
    }
    if (request.fireDate() != null) {
      employee.setFireDate(request.fireDate());
    }
    
    // Валидация логики дат после обновления
    if (birthDate != null && hireDate != null && hireDate.isBefore(birthDate)) {
      throw new BadRequestException("Hire date cannot be before birth date");
    }
    
    if (hireDate != null && fireDate != null && fireDate.isBefore(hireDate)) {
      throw new BadRequestException("Fire date cannot be before hire date");
    }
    
    // Валидация возраста при обновлении
    if (birthDate != null && hireDate != null) {
      long years = java.time.temporal.ChronoUnit.YEARS.between(birthDate, hireDate);
      if (years < 16) {
        throw new BadRequestException("Employee must be at least 16 years old at hire date");
      }
    }
    
    if (request.competenceRank() != null) {
      employee.setCompetenceRank(request.competenceRank());
    }
    
    if (request.competenceLevel() != null) {
      if (request.competenceLevel() < 1 || request.competenceLevel() > 3) {
        throw new BadRequestException("Competence level must be between 1 and 3");
      }
      employee.setCompetenceLevel(request.competenceLevel());
    }
    
    if (request.departmentId() != null) {
      Department department = departmentRepository.findById(request.departmentId())
          .orElseThrow(() -> new ResourceNotFoundException("Department", request.departmentId()));
      employee.setDepartment(department);
    }

    Employee updated = employeeRepository.save(employee);
    return toDto(updated);
  }

  public void deleteEmployee(Long id) {
    Employee employee = employeeRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Employee", id));
    employeeRepository.delete(employee);
  }

  private EmployeeDto toDto(Employee employee) {
    return EmployeeDto.of(
        employee.getId(),
        employee.getUser() != null ? employee.getUser().getId() : null,
        employee.getFullName(),
        employee.getGender(),
        employee.getBirthDate(),
        employee.getHireDate(),
        employee.getFireDate(),
        employee.getCompetenceRank(),
        employee.getCompetenceLevel(),
        employee.getDepartment() != null ? employee.getDepartment().getId() : null
    );
  }
}

