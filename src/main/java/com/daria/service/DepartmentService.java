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
    if (departmentRepository.findByName(request.name()).isPresent()) {
      throw new BadRequestException("Department with name '" + request.name() + "' already exists");
    }

    Employee head = null;
    if (request.headId() != null) {
      head = employeeRepository.findById(request.headId())
          .orElseThrow(() -> new ResourceNotFoundException("Employee", request.headId()));
    }

    Department department = Department.builder()
        .name(request.name())
        .head(head)
        .build();

    Department saved = departmentRepository.save(department);
    return toDto(saved);
  }

  public DepartmentDto updateDepartment(Long id, DepartmentUpdateRequest request) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Department", id));

    if (request.name() != null && !request.name().equals(department.getName())) {
      if (departmentRepository.findByName(request.name()).isPresent()) {
        throw new BadRequestException("Department with name '" + request.name() + "' already exists");
      }
      department.setName(request.name());
    }

    if (request.headId() != null) {
      Employee head = employeeRepository.findById(request.headId())
          .orElseThrow(() -> new ResourceNotFoundException("Employee", request.headId()));
      department.setHead(head);
    }

    Department updated = departmentRepository.save(department);
    return toDto(updated);
  }

  public void deleteDepartment(Long id) {
    Department department = departmentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Department", id));
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

