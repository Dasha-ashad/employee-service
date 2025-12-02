package com.daria.controller;

import com.daria.dto.DepartmentCreateRequest;
import com.daria.dto.DepartmentDto;
import com.daria.dto.DepartmentUpdateRequest;
import com.daria.service.DepartmentService;
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
@RequestMapping("/v1/employee-service/departments")
@RequiredArgsConstructor
@Tag(name = "Departments", description = "API для управления отделами")
@SecurityRequirement(name = "bearerAuth")
public class DepartmentController {

  private final DepartmentService departmentService;

  @Operation(summary = "Получить все отделы", description = "Возвращает список всех отделов")
  @ApiResponse(responseCode = "200", description = "Список отделов успешно получен")
  @GetMapping
  public ResponseEntity<List<DepartmentDto>> getAllDepartments() {
    List<DepartmentDto> departments = departmentService.getAllDepartments();
    return ResponseEntity.ok(departments);
  }

  @Operation(summary = "Получить отдел по ID", description = "Возвращает информацию об отделе по его идентификатору")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Отдел найден",
          content = @Content(schema = @Schema(implementation = DepartmentDto.class))
      ),
      @ApiResponse(responseCode = "404", description = "Отдел не найден")
  })
  @GetMapping("/{id}")
  public ResponseEntity<DepartmentDto> getDepartmentById(@PathVariable Long id) {
    DepartmentDto department = departmentService.getDepartmentById(id);
    return ResponseEntity.ok(department);
  }

  @Operation(summary = "Создать новый отдел", description = "Создает новый отдел в системе")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Отдел успешно создан",
          content = @Content(schema = @Schema(implementation = DepartmentDto.class))
      ),
      @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
  })
  @PostMapping
  public ResponseEntity<DepartmentDto> createDepartment(@Valid @RequestBody DepartmentCreateRequest request) {
    DepartmentDto created = departmentService.createDepartment(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @Operation(summary = "Обновить отдел", description = "Обновляет информацию об отделе")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Отдел успешно обновлен",
          content = @Content(schema = @Schema(implementation = DepartmentDto.class))
      ),
      @ApiResponse(responseCode = "404", description = "Отдел не найден"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
  })
  @PutMapping("/{id}")
  public ResponseEntity<DepartmentDto> updateDepartment(
      @PathVariable Long id,
      @Valid @RequestBody DepartmentUpdateRequest request) {
    DepartmentDto updated = departmentService.updateDepartment(id, request);
    return ResponseEntity.ok(updated);
  }

  @Operation(summary = "Удалить отдел", description = "Удаляет отдел из системы")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Отдел успешно удален"),
      @ApiResponse(responseCode = "404", description = "Отдел не найден")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteDepartment(@PathVariable Long id) {
    departmentService.deleteDepartment(id);
    return ResponseEntity.noContent().build();
  }
}

