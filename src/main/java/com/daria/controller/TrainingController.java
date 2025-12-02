package com.daria.controller;

import com.daria.dto.TrainingCreateRequest;
import com.daria.dto.TrainingDto;
import com.daria.dto.TrainingUpdateRequest;
import com.daria.service.TrainingService;
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
@RequestMapping("/v1/employee-service/trainings")
@RequiredArgsConstructor
@Tag(name = "Trainings", description = "API для управления обучениями сотрудников")
@SecurityRequirement(name = "bearerAuth")
public class TrainingController {

  private final TrainingService trainingService;

  @Operation(summary = "Получить все обучения", description = "Возвращает список всех обучений")
  @ApiResponse(responseCode = "200", description = "Список обучений успешно получен")
  @GetMapping
  public ResponseEntity<List<TrainingDto>> getAllTrainings() {
    List<TrainingDto> trainings = trainingService.getAllTrainings();
    return ResponseEntity.ok(trainings);
  }

  @Operation(summary = "Получить обучение по ID", description = "Возвращает информацию об обучении по его идентификатору")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Обучение найдено",
          content = @Content(schema = @Schema(implementation = TrainingDto.class))
      ),
      @ApiResponse(responseCode = "404", description = "Обучение не найдено")
  })
  @GetMapping("/{id}")
  public ResponseEntity<TrainingDto> getTrainingById(@PathVariable Long id) {
    TrainingDto training = trainingService.getTrainingById(id);
    return ResponseEntity.ok(training);
  }

  @Operation(summary = "Получить обучения сотрудника", description = "Возвращает список всех обучений конкретного сотрудника")
  @ApiResponse(responseCode = "200", description = "Список обучений сотрудника успешно получен")
  @GetMapping("/employee/{employeeId}")
  public ResponseEntity<List<TrainingDto>> getTrainingsByEmployeeId(@PathVariable Long employeeId) {
    List<TrainingDto> trainings = trainingService.getTrainingsByEmployeeId(employeeId);
    return ResponseEntity.ok(trainings);
  }

  @Operation(summary = "Создать новое обучение", description = "Создает новое обучение для сотрудника. Доступно администраторам и руководителям отделов.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "201",
          description = "Обучение успешно создано",
          content = @Content(schema = @Schema(implementation = TrainingDto.class))
      ),
      @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
      @ApiResponse(responseCode = "404", description = "Сотрудник не найден"),
      @ApiResponse(responseCode = "403", description = "Доступ запрещен. Требуется роль ADMIN или HEAD")
  })
  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'HEAD')") // Только администраторы и руководители могут создавать обучения
  public ResponseEntity<TrainingDto> createTraining(@Valid @RequestBody TrainingCreateRequest request) {
    TrainingDto created = trainingService.createTraining(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @Operation(summary = "Обновить обучение", description = "Обновляет информацию об обучении. Доступно администраторам и руководителям отделов.")
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Обучение успешно обновлено",
          content = @Content(schema = @Schema(implementation = TrainingDto.class))
      ),
      @ApiResponse(responseCode = "404", description = "Обучение не найдено"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
      @ApiResponse(responseCode = "403", description = "Доступ запрещен. Требуется роль ADMIN или HEAD")
  })
  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'HEAD')") // Только администраторы и руководители могут обновлять обучения
  public ResponseEntity<TrainingDto> updateTraining(
      @PathVariable Long id,
      @Valid @RequestBody TrainingUpdateRequest request) {
    TrainingDto updated = trainingService.updateTraining(id, request);
    return ResponseEntity.ok(updated);
  }

  @Operation(summary = "Удалить обучение", description = "Удаляет обучение из системы. Доступно администраторам и руководителям отделов.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Обучение успешно удалено"),
      @ApiResponse(responseCode = "404", description = "Обучение не найдено"),
      @ApiResponse(responseCode = "403", description = "Доступ запрещен. Требуется роль ADMIN или HEAD")
  })
  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'HEAD')") // Только администраторы и руководители могут удалять обучения
  public ResponseEntity<Void> deleteTraining(@PathVariable Long id) {
    trainingService.deleteTraining(id);
    return ResponseEntity.noContent().build();
  }
}

