package com.daria.controller;

import com.daria.dto.EmployeeCreateRequest;
import com.daria.dto.EmployeeDto;
import com.daria.dto.EmployeeUpdateRequest;
import com.daria.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/employee-service/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "API для управления сотрудниками")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

  private final EmployeeService employeeService;

  @Operation(
      summary = "Получить всех сотрудников", 
      description = "Возвращает список всех сотрудников с опциональной фильтрацией и сортировкой. " +
          "Доступно всем аутентифицированным пользователям. " +
          "Параметры фильтрации: search (поиск по имени), departmentId (фильтр по отделу), " +
          "gender (фильтр по полу: М или Ж), sortBy (сортировка: absences или competence_level).")
  @ApiResponse(responseCode = "200", description = "Список сотрудников успешно получен")
  @GetMapping
  // Просмотр доступен всем аутентифицированным пользователям
  public ResponseEntity<List<EmployeeDto>> getAllEmployees(
      @RequestParam(required = false) String search,
      @RequestParam(required = false) Long departmentId,
      @RequestParam(required = false) String gender,
      @RequestParam(required = false) String sortBy) {
    
    // Валидация и нормализация параметров
    // search: нормализуем пустые строки в null
    String normalizedSearch = (search != null && search.trim().isEmpty()) ? null : search;
    
    // departmentId: проверяем, что это валидное положительное число
    Long normalizedDepartmentId = null;
    if (departmentId != null && departmentId > 0) {
      normalizedDepartmentId = departmentId;
    }
    
    // gender: преобразуем строку в enum, если указана
    // ВАЖНО: Gender enum использует кириллицу (М, Ж), поэтому сравниваем напрямую
    com.daria.entity.enums.Gender genderEnum = null;
    if (gender != null && !gender.trim().isEmpty()) {
      String trimmedGender = gender.trim();
      // Проверяем значения enum (М или Ж)
      if ("М".equals(trimmedGender)) {
        genderEnum = com.daria.entity.enums.Gender.М;
      } else if ("Ж".equals(trimmedGender)) {
        genderEnum = com.daria.entity.enums.Gender.Ж;
      }
      // Если значение невалидное, оставляем null (игнорируем фильтр)
    }
    
    // sortBy: проверяем, что это валидное значение
    String normalizedSortBy = null;
    if (sortBy != null && !sortBy.trim().isEmpty()) {
      String trimmedSortBy = sortBy.trim();
      if ("absences".equals(trimmedSortBy) || "competence_level".equals(trimmedSortBy)) {
        normalizedSortBy = trimmedSortBy;
      }
      // Если значение невалидное, игнорируем сортировку
    }
    
    List<EmployeeDto> employees = employeeService.getAllEmployees(
        normalizedSearch, normalizedDepartmentId, genderEnum, normalizedSortBy);
    return ResponseEntity.ok(employees);
  }

  @Operation(summary = "Получить сотрудника по ID", description = "Возвращает информацию о сотруднике по его идентификатору")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Сотрудник найден",
          content = @Content(schema = @Schema(implementation = EmployeeDto.class))
      ),
      @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
  })
  @GetMapping("/{id}")
  public ResponseEntity<EmployeeDto> getEmployeeById(@PathVariable Long id) {
    EmployeeDto employee = employeeService.getEmployeeById(id);
    return ResponseEntity.ok(employee);
  }

  @Operation(summary = "Создать нового сотрудника", description = "Создает нового сотрудника в системе. Доступно администраторам и руководителям отделов.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Сотрудник успешно создан",
          content = @Content(schema = @Schema(implementation = EmployeeDto.class))
      ),
      @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
      @ApiResponse(responseCode = "403", description = "Доступ запрещен. Требуется роль ADMIN или HEAD")
  })
  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'HEAD')") // Только администраторы и руководители могут создавать сотрудников
  public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeCreateRequest request) {
    EmployeeDto created = employeeService.createEmployee(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @Operation(summary = "Обновить сотрудника", description = "Обновляет информацию о сотруднике. Доступно администраторам и руководителям отделов.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Сотрудник успешно обновлен",
          content = @Content(schema = @Schema(implementation = EmployeeDto.class))
      ),
      @ApiResponse(responseCode = "404", description = "Сотрудник не найден"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
      @ApiResponse(responseCode = "403", description = "Доступ запрещен. Требуется роль ADMIN или HEAD")
  })
  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'HEAD')") // Только администраторы и руководители могут обновлять сотрудников
  public ResponseEntity<EmployeeDto> updateEmployee(
      @PathVariable Long id,
      @Valid @RequestBody EmployeeUpdateRequest request) {
    EmployeeDto updated = employeeService.updateEmployee(id, request);
    return ResponseEntity.ok(updated);
  }

  @Operation(summary = "Удалить сотрудника", description = "Удаляет сотрудника из системы. Доступно только администраторам.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Сотрудник успешно удален"),
      @ApiResponse(responseCode = "404", description = "Сотрудник не найден"),
      @ApiResponse(responseCode = "403", description = "Доступ запрещен. Требуется роль ADMIN")
  })
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')") // Только администраторы могут удалять сотрудников
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.noContent().build();
  }
}

