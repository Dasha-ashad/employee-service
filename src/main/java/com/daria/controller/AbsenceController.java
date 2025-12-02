package com.daria.controller;

import com.daria.dto.AbsenceCreateRequest;
import com.daria.dto.AbsenceDto;
import com.daria.dto.AbsenceUpdateRequest;
import com.daria.service.AbsenceService;
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
@RequestMapping("/v1/employee-service/absences")
@RequiredArgsConstructor
@Tag(name = "Absences", description = "API для управления пропусками сотрудников (отпуска, больничные и т.д.)")
@SecurityRequirement(name = "bearerAuth")
public class AbsenceController {

  private final AbsenceService absenceService;

  @Operation(summary = "Получить все пропуски", description = "Возвращает список всех пропусков сотрудников")
  @ApiResponse(responseCode = "200", description = "Список пропусков успешно получен")
  @GetMapping
  public ResponseEntity<List<AbsenceDto>> getAllAbsences() {
    List<AbsenceDto> absences = absenceService.getAllAbsences();
    return ResponseEntity.ok(absences);
  }

  @Operation(summary = "Получить пропуск по ID", description = "Возвращает информацию о пропуске по его идентификатору")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Пропуск найден",
          content = @Content(schema = @Schema(implementation = AbsenceDto.class))
      ),
      @ApiResponse(responseCode = "404", description = "Пропуск не найден")
  })
  @GetMapping("/{id}")
  public ResponseEntity<AbsenceDto> getAbsenceById(@PathVariable Long id) {
    AbsenceDto absence = absenceService.getAbsenceById(id);
    return ResponseEntity.ok(absence);
  }

  @Operation(summary = "Получить пропуски сотрудника", description = "Возвращает список всех пропусков конкретного сотрудника")
  @ApiResponse(responseCode = "200", description = "Список пропусков сотрудника успешно получен")
  @GetMapping("/employee/{employeeId}")
  public ResponseEntity<List<AbsenceDto>> getAbsencesByEmployeeId(@PathVariable Long employeeId) {
    List<AbsenceDto> absences = absenceService.getAbsencesByEmployeeId(employeeId);
    return ResponseEntity.ok(absences);
  }

  @Operation(summary = "Создать новый пропуск", description = "Создает новую запись о пропуске для сотрудника. Доступно администраторам и руководителям отделов.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Пропуск успешно создан",
          content = @Content(schema = @Schema(implementation = AbsenceDto.class))
      ),
      @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
      @ApiResponse(responseCode = "404", description = "Сотрудник не найден"),
      @ApiResponse(responseCode = "403", description = "Доступ запрещен. Требуется роль ADMIN или HEAD")
  })
  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'HEAD')") // Только администраторы и руководители могут создавать пропуски
  public ResponseEntity<AbsenceDto> createAbsence(@Valid @RequestBody AbsenceCreateRequest request) {
    AbsenceDto created = absenceService.createAbsence(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @Operation(summary = "Обновить пропуск", description = "Обновляет информацию о пропуске. Доступно администраторам и руководителям отделов.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Пропуск успешно обновлен",
          content = @Content(schema = @Schema(implementation = AbsenceDto.class))
      ),
      @ApiResponse(responseCode = "404", description = "Пропуск не найден"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
      @ApiResponse(responseCode = "403", description = "Доступ запрещен. Требуется роль ADMIN или HEAD")
  })
  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'HEAD')") // Только администраторы и руководители могут обновлять пропуски
  public ResponseEntity<AbsenceDto> updateAbsence(
      @PathVariable Long id,
      @Valid @RequestBody AbsenceUpdateRequest request) {
    AbsenceDto updated = absenceService.updateAbsence(id, request);
    return ResponseEntity.ok(updated);
  }

  @Operation(summary = "Удалить пропуск", description = "Удаляет пропуск из системы. Доступно администраторам и руководителям отделов.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Пропуск успешно удален"),
      @ApiResponse(responseCode = "404", description = "Пропуск не найден"),
      @ApiResponse(responseCode = "403", description = "Доступ запрещен. Требуется роль ADMIN или HEAD")
  })
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'HEAD')") // Только администраторы и руководители могут удалять пропуски
  public ResponseEntity<Void> deleteAbsence(@PathVariable Long id) {
    absenceService.deleteAbsence(id);
    return ResponseEntity.noContent().build();
  }
}

