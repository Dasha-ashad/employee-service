package com.daria.dto;

import java.util.List;

/**
 * DTO для аналитических данных
 * 
 * Содержит агрегированные данные для отображения на странице аналитики:
 * - KPI метрики (общее количество сотрудников, средняя компетенция, текучесть)
 * - Данные для графиков (возраст, пол, ранги, отделы)
 * - Сводные таблицы
 */
public record AnalyticsDto(
    // KPI метрики
    Long totalEmployees,
    Double avgCompetence,
    Long trained,
    Long fired,
    Double turnoverRate,
    AbsencesInfo absences,
    
    // Данные для графиков
    List<ChartData> gender,
    List<ChartData> age,
    List<ChartData> ranks,
    List<DepartmentChartData> departments,
    List<HiresFiresData> hiresFires,
    
    // Сводные таблицы
    List<DepartmentSummary> departmentsSummary,
    List<EmployeeSummary> employees
) {
    
    /**
     * Информация о пропусках
     */
    public record AbsencesInfo(
        Long valid,
        Long invalid
    ) {}
    
    /**
     * Данные для графиков (pie, bar)
     */
    public record ChartData(
        String name,
        Long value
    ) {}
    
    /**
     * Данные по отделам для графиков
     */
    public record DepartmentChartData(
        String dept,
        Long employees
    ) {}
    
    /**
     * Данные о приемах и увольнениях
     */
    public record HiresFiresData(
        String month,
        Long hired,
        Long fired
    ) {}
    
    /**
     * Сводка по отделу
     */
    public record DepartmentSummary(
        String dept,
        Long count,
        Double avgComp
    ) {}
    
    /**
     * Сводка по сотруднику
     */
    public record EmployeeSummary(
        String name,
        Integer competence,
        String training,
        Long absences
    ) {}
}

