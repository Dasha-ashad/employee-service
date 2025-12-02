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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/employee-service/employees")
@RequiredArgsConstructor
@Tag(name = "Employees", description = "API для управления сотрудниками")
@SecurityRequirement(name = "bearerAuth")
public class EmployeeController {

  private final EmployeeService employeeService;

  @Operation(summary = "Получить всех сотрудников", description = "Возвращает список всех сотрудников")
  @ApiResponse(responseCode = "200", description = "Список сотрудников успешно получен")
  @GetMapping
  public ResponseEntity<List<EmployeeDto>> getAllEmployees() {
    List<EmployeeDto> employees = employeeService.getAllEmployees();
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

  @Operation(summary = "Создать нового сотрудника", description = "Создает нового сотрудника в системе")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Сотрудник успешно создан",
          content = @Content(schema = @Schema(implementation = EmployeeDto.class))
      ),
      @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
  })
  @PostMapping
  public ResponseEntity<EmployeeDto> createEmployee(@Valid @RequestBody EmployeeCreateRequest request) {
    EmployeeDto created = employeeService.createEmployee(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @Operation(summary = "Обновить сотрудника", description = "Обновляет информацию о сотруднике")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Сотрудник успешно обновлен",
          content = @Content(schema = @Schema(implementation = EmployeeDto.class))
      ),
      @ApiResponse(responseCode = "404", description = "Сотрудник не найден"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
  })
  @PutMapping("/{id}")
  public ResponseEntity<EmployeeDto> updateEmployee(
      @PathVariable Long id,
      @Valid @RequestBody EmployeeUpdateRequest request) {
    EmployeeDto updated = employeeService.updateEmployee(id, request);
    return ResponseEntity.ok(updated);
  }

  @Operation(summary = "Удалить сотрудника", description = "Удаляет сотрудника из системы")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Сотрудник успешно удален"),
      @ApiResponse(responseCode = "404", description = "Сотрудник не найден")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    employeeService.deleteEmployee(id);
    return ResponseEntity.noContent().build();
  }
}

