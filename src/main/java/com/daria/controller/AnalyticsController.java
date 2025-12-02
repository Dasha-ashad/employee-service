package com.daria.controller;

import com.daria.dto.AnalyticsDto;
import com.daria.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для аналитических данных
 * 
 * Предоставляет агрегированные данные для страницы аналитики:
 * - KPI метрики
 * - Данные для графиков
 * - Сводные таблицы
 * 
 * Поддерживает фильтрацию:
 * - По отделу (departmentId)
 * - По периоду (period: month, quarter, year)
 */
@RestController
@RequestMapping("/v1/employee-service/analytics")
@RequiredArgsConstructor
@Tag(name = "Analytics", description = "API для получения аналитических данных")
@SecurityRequirement(name = "bearerAuth")
public class AnalyticsController {

  private final AnalyticsService analyticsService;

  @Operation(
      summary = "Получить аналитические данные",
      description = "Возвращает агрегированные аналитические данные с опциональной фильтрацией. " +
          "Доступно всем аутентифицированным пользователям. " +
          "Параметры фильтрации: departmentId (фильтр по отделу), period (период: month, quarter, year).")
  @ApiResponse(responseCode = "200", description = "Аналитические данные успешно получены")
  @GetMapping
  public ResponseEntity<AnalyticsDto> getAnalytics(
      @RequestParam(required = false) Long departmentId,
      @RequestParam(required = false) String period) {
    
    // Валидация и нормализация параметров
    Long normalizedDepartmentId = null;
    if (departmentId != null && departmentId > 0) {
      normalizedDepartmentId = departmentId;
    }
    
    String normalizedPeriod = null;
    if (period != null && !period.trim().isEmpty()) {
      String trimmedPeriod = period.trim().toLowerCase();
      if ("month".equals(trimmedPeriod) || "quarter".equals(trimmedPeriod) || "year".equals(trimmedPeriod)) {
        normalizedPeriod = trimmedPeriod;
      }
      // Если период невалидный, игнорируем его
    }
    
    AnalyticsDto analytics = analyticsService.getAnalytics(normalizedDepartmentId, normalizedPeriod);
    return ResponseEntity.ok(analytics);
  }
}

