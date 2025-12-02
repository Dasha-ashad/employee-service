package com.daria.service;

import com.daria.dto.AnalyticsDto;
import com.daria.entity.AbsenceEntity;
import com.daria.entity.Employee;
import com.daria.entity.Training;
import com.daria.entity.enums.CompetenceRank;
import com.daria.entity.enums.Gender;
import com.daria.repository.AbsenceRepository;
import com.daria.repository.DepartmentRepository;
import com.daria.repository.EmployeeRepository;
import com.daria.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Сервис для расчета аналитических данных
 * 
 * Лучшие практики:
 * - Фильтрация данных на уровне БД для производительности
 * - Кэширование результатов (можно добавить в будущем)
 * - Обработка edge cases (пустые данные, null значения)
 * - Поддержка фильтрации по отделу и периоду
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AnalyticsService {

  private final EmployeeRepository employeeRepository;
  private final DepartmentRepository departmentRepository;
  private final AbsenceRepository absenceRepository;
  private final TrainingRepository trainingRepository;

  /**
   * Получить аналитические данные с фильтрацией
   * 
   * @param departmentId фильтр по отделу (null = все отделы)
   * @param period период: "month", "quarter", "year" (null = все время)
   * @return аналитические данные
   */
  public AnalyticsDto getAnalytics(Long departmentId, String period) {
    // Получаем список сотрудников с учетом фильтров
    List<Employee> employees = getFilteredEmployees(departmentId, period);
    
    // Получаем все пропуски для расчета статистики
    List<AbsenceEntity> allAbsences = absenceRepository.findAll();
    
    // Получаем все обучения
    List<Training> allTrainings = trainingRepository.findAll();
    
    // Вычисляем KPI метрики
    Long totalEmployees = (long) employees.size();
    Double avgCompetence = calculateAvgCompetence(employees);
    Long trained = calculateTrainedCount(employees, allTrainings, period);
    Long fired = calculateFiredCount(employees, period);
    Double turnoverRate = calculateTurnoverRate(totalEmployees, fired);
    AnalyticsDto.AbsencesInfo absences = calculateAbsencesInfo(employees, allAbsences);
    
    // Данные для графиков
    List<AnalyticsDto.ChartData> gender = calculateGenderDistribution(employees);
    List<AnalyticsDto.ChartData> age = calculateAgeDistribution(employees);
    List<AnalyticsDto.ChartData> ranks = calculateRankDistribution(employees);
    List<AnalyticsDto.DepartmentChartData> departments = calculateDepartmentDistribution(employees);
    List<AnalyticsDto.HiresFiresData> hiresFires = calculateHiresFires(employees, period);
    
    // Сводные таблицы
    List<AnalyticsDto.DepartmentSummary> departmentsSummary = calculateDepartmentsSummary(employees);
    List<AnalyticsDto.EmployeeSummary> employeesSummary = calculateEmployeesSummary(employees, allTrainings, allAbsences);
    
    return new AnalyticsDto(
        totalEmployees,
        avgCompetence,
        trained,
        fired,
        turnoverRate,
        absences,
        gender,
        age,
        ranks,
        departments,
        hiresFires,
        departmentsSummary,
        employeesSummary
    );
  }

  /**
   * Получить отфильтрованных сотрудников
   */
  private List<Employee> getFilteredEmployees(Long departmentId, String period) {
    List<Employee> allEmployees = employeeRepository.findAll();
    
    // Фильтр по отделу
    if (departmentId != null) {
      allEmployees = allEmployees.stream()
          .filter(e -> e.getDepartment() != null && e.getDepartment().getId().equals(departmentId))
          .collect(Collectors.toList());
    }
    
    // Фильтр по периоду (для дат приема/увольнения и активных сотрудников)
    if (period != null && !period.isEmpty()) {
      LocalDate periodStart = getPeriodStart(period);
      if (periodStart != null) {
        allEmployees = allEmployees.stream()
            .filter(e -> {
              // Включаем сотрудника, если:
              // 1. Он был принят в периоде (hireDate >= periodStart)
              if (e.getHireDate() != null && !e.getHireDate().isBefore(periodStart)) {
                return true;
              }
              // 2. Он был уволен в периоде (fireDate >= periodStart)
              if (e.getFireDate() != null && !e.getFireDate().isBefore(periodStart)) {
                return true;
              }
              // 3. Он был активен в течение периода (принят до периода, не уволен или уволен после периода)
              if (e.getHireDate() != null && e.getHireDate().isBefore(periodStart)) {
                return e.getFireDate() == null || e.getFireDate().isAfter(periodStart);
              }
              // Иначе исключаем
              return false;
            })
            .collect(Collectors.toList());
      }
    }
    
    return allEmployees;
  }

  /**
   * Получить начало периода
   */
  private LocalDate getPeriodStart(String period) {
    LocalDate now = LocalDate.now();
    return switch (period.toLowerCase()) {
      case "month" -> now.minusMonths(1);
      case "quarter" -> now.minusMonths(3);
      case "year" -> now.minusYears(1);
      default -> null;
    };
  }

  /**
   * Вычислить среднюю компетенцию
   */
  private Double calculateAvgCompetence(List<Employee> employees) {
    if (employees.isEmpty()) {
      return 0.0;
    }
    
    double sum = employees.stream()
        .filter(e -> e.getCompetenceLevel() != null)
        .mapToInt(Employee::getCompetenceLevel)
        .sum();
    
    long count = employees.stream()
        .filter(e -> e.getCompetenceLevel() != null)
        .count();
    
    return count > 0 ? sum / count : 0.0;
  }

  /**
   * Вычислить количество обученных сотрудников
   * 
   * Edge cases:
   * - Обработка null дат
   * - Учет периода фильтрации
   * - Обработка пустых списков
   */
  private Long calculateTrainedCount(List<Employee> employees, List<Training> allTrainings, String period) {
    if (employees.isEmpty()) {
      return 0L;
    }
    
    LocalDate periodStart = getPeriodStart(period);
    
    // Получаем ID сотрудников, которые прошли обучение в указанном периоде
    Set<Long> trainedEmployeeIds = allTrainings.stream()
        .filter(t -> {
          // Проверяем, что у обучения есть сотрудник и дата начала
          if (t.getEmployee() == null || t.getStartDate() == null) {
            return false;
          }
          
          // Если период указан, учитываем только обучения в периоде
          if (periodStart != null) {
            return !t.getStartDate().isBefore(periodStart);
          }
          // Если период не указан, учитываем все обучения
          return true;
        })
        .map(t -> t.getEmployee().getId())
        .collect(Collectors.toSet());
    
    // Подсчитываем, сколько из отфильтрованных сотрудников прошли обучение
    return employees.stream()
        .filter(e -> trainedEmployeeIds.contains(e.getId()))
        .count();
  }

  /**
   * Вычислить количество уволенных
   * 
   * Edge cases:
   * - Обработка null дат увольнения
   * - Правильная фильтрация по периоду (включая начало периода)
   * - Учет текущей даты (не учитываем будущие даты)
   */
  private Long calculateFiredCount(List<Employee> employees, String period) {
    LocalDate periodStart = getPeriodStart(period);
    LocalDate now = LocalDate.now();
    
    return employees.stream()
        .filter(e -> {
          if (e.getFireDate() == null) {
            return false;
          }
          // Не учитываем будущие даты увольнения
          if (e.getFireDate().isAfter(now)) {
            return false;
          }
          // Если период указан, учитываем только увольнения в периоде
          if (periodStart != null) {
            // Включаем увольнения, которые произошли в периоде или после начала периода
            return !e.getFireDate().isBefore(periodStart);
          }
          // Если период не указан, учитываем все увольнения
          return true;
        })
        .count();
  }

  /**
   * Вычислить текучесть кадров (%)
   */
  private Double calculateTurnoverRate(Long totalEmployees, Long fired) {
    if (totalEmployees == null || totalEmployees == 0) {
      return 0.0;
    }
    return (fired.doubleValue() / totalEmployees.doubleValue()) * 100.0;
  }

  /**
   * Вычислить информацию о пропусках
   * 
   * Edge cases:
   * - Обработка null статусов
   * - Безопасная проверка enum значений
   */
  private AnalyticsDto.AbsencesInfo calculateAbsencesInfo(List<Employee> employees, List<AbsenceEntity> allAbsences) {
    if (employees.isEmpty()) {
      return new AnalyticsDto.AbsencesInfo(0L, 0L);
    }
    
    Set<Long> employeeIds = employees.stream()
        .map(Employee::getId)
        .collect(Collectors.toSet());
    
    List<AbsenceEntity> relevantAbsences = allAbsences.stream()
        .filter(a -> a.getEmployee() != null && employeeIds.contains(a.getEmployee().getId()))
        .collect(Collectors.toList());
    
    // Разделяем на валидные (GOOD_REASON) и невалидные (BAD_REASON)
    // Используем безопасную проверку enum
    long valid = relevantAbsences.stream()
        .filter(a -> {
          if (a.getStatus() == null) {
            return false; // Если статус не указан, считаем невалидным
          }
          return a.getStatus() == com.daria.entity.enums.AbsenceStatus.GOOD_REASON;
        })
        .count();
    
    long invalid = relevantAbsences.size() - valid;
    
    return new AnalyticsDto.AbsencesInfo(valid, invalid);
  }

  /**
   * Вычислить распределение по полу
   * 
   * Edge cases:
   * - Обработка null значений gender
   * - Пустые списки
   */
  private List<AnalyticsDto.ChartData> calculateGenderDistribution(List<Employee> employees) {
    if (employees.isEmpty()) {
      return new ArrayList<>();
    }
    
    Map<Gender, Long> distribution = employees.stream()
        .filter(e -> e.getGender() != null) // Исключаем null значения
        .collect(Collectors.groupingBy(
            Employee::getGender,
            Collectors.counting()
        ));
    
    return distribution.entrySet().stream()
        .map(e -> new AnalyticsDto.ChartData(
            e.getKey() == Gender.М ? "Мужчины" : "Женщины",
            e.getValue()
        ))
        .collect(Collectors.toList());
  }

  /**
   * Вычислить распределение по возрасту
   */
  private List<AnalyticsDto.ChartData> calculateAgeDistribution(List<Employee> employees) {
    LocalDate now = LocalDate.now();
    Map<String, Long> ageGroups = new LinkedHashMap<>();
    ageGroups.put("18–25", 0L);
    ageGroups.put("26–35", 0L);
    ageGroups.put("36–45", 0L);
    ageGroups.put("46+", 0L);
    
    employees.forEach(e -> {
      if (e.getBirthDate() != null) {
        long age = ChronoUnit.YEARS.between(e.getBirthDate(), now);
        if (age >= 18 && age <= 25) {
          ageGroups.put("18–25", ageGroups.get("18–25") + 1);
        } else if (age <= 35) {
          ageGroups.put("26–35", ageGroups.get("26–35") + 1);
        } else if (age <= 45) {
          ageGroups.put("36–45", ageGroups.get("36–45") + 1);
        } else {
          ageGroups.put("46+", ageGroups.get("46+") + 1);
        }
      }
    });
    
    return ageGroups.entrySet().stream()
        .map(e -> new AnalyticsDto.ChartData(e.getKey(), e.getValue()))
        .collect(Collectors.toList());
  }

  /**
   * Вычислить распределение по рангам
   */
  private List<AnalyticsDto.ChartData> calculateRankDistribution(List<Employee> employees) {
    Map<String, Long> distribution = employees.stream()
        .collect(Collectors.groupingBy(
            e -> {
              CompetenceRank rank = e.getCompetenceRank();
              if (rank == null) return "Не указан";
              return switch (rank) {
                case JUNIOR -> "Начальный";
                case MIDDLE -> "Средний";
                case SENIOR -> "Высокий";
              };
            },
            Collectors.counting()
        ));
    
    return distribution.entrySet().stream()
        .map(e -> new AnalyticsDto.ChartData(e.getKey(), e.getValue()))
        .collect(Collectors.toList());
  }

  /**
   * Вычислить распределение по отделам
   * 
   * Edge cases:
   * - Обработка null отделов
   * - Обработка null имен отделов
   * - Пустые списки
   */
  private List<AnalyticsDto.DepartmentChartData> calculateDepartmentDistribution(List<Employee> employees) {
    if (employees.isEmpty()) {
      return new ArrayList<>();
    }
    
    Map<String, Long> distribution = employees.stream()
        .filter(e -> e.getDepartment() != null && e.getDepartment().getName() != null)
        .collect(Collectors.groupingBy(
            e -> e.getDepartment().getName(),
            Collectors.counting()
        ));
    
    return distribution.entrySet().stream()
        .map(e -> new AnalyticsDto.DepartmentChartData(e.getKey(), e.getValue()))
        .collect(Collectors.toList());
  }

  /**
   * Вычислить данные о приемах и увольнениях
   * 
   * Edge cases:
   * - Учитывает период фильтрации
   * - Обрабатывает null даты
   * - Фильтрует данные по периоду, если указан
   * - Показывает только месяцы в пределах периода
   * - Группирует по месяцам с учетом года
   */
  private List<AnalyticsDto.HiresFiresData> calculateHiresFires(List<Employee> employees, String period) {
    List<AnalyticsDto.HiresFiresData> result = new ArrayList<>();
    LocalDate periodStart = getPeriodStart(period);
    LocalDate now = LocalDate.now();
    
    // Группируем по месяцам с учетом года (ключ: "YYYY-MM")
    Map<String, Long> hiresByMonth = new LinkedHashMap<>();
    Map<String, Long> firesByMonth = new LinkedHashMap<>();
    
    // Определяем диапазон месяцев для отображения
    LocalDate startDate = periodStart != null ? periodStart : LocalDate.of(now.getYear(), 1, 1);
    LocalDate endDate = now;
    
    // Инициализируем все месяцы в диапазоне
    LocalDate current = startDate.withDayOfMonth(1);
    while (!current.isAfter(endDate)) {
      String monthKey = String.format("%d-%02d", current.getYear(), current.getMonthValue());
      hiresByMonth.put(monthKey, 0L);
      firesByMonth.put(monthKey, 0L);
      current = current.plusMonths(1);
    }
    
    String[] monthNames = {"Янв", "Фев", "Мар", "Апр", "Май", "Июн", "Июл", "Авг", "Сен", "Окт", "Ноя", "Дек"};
    
    // Подсчитываем приемы и увольнения
    employees.forEach(e -> {
      // Приемы
      if (e.getHireDate() != null) {
        LocalDate hireDate = e.getHireDate();
        // Проверяем, что дата в пределах периода и не в будущем
        if (!hireDate.isAfter(now) && (periodStart == null || !hireDate.isBefore(periodStart))) {
          String monthKey = String.format("%d-%02d", hireDate.getYear(), hireDate.getMonthValue());
          hiresByMonth.put(monthKey, hiresByMonth.getOrDefault(monthKey, 0L) + 1);
        }
      }
      
      // Увольнения
      if (e.getFireDate() != null) {
        LocalDate fireDate = e.getFireDate();
        // Проверяем, что дата в пределах периода и не в будущем
        if (!fireDate.isAfter(now) && (periodStart == null || !fireDate.isBefore(periodStart))) {
          String monthKey = String.format("%d-%02d", fireDate.getYear(), fireDate.getMonthValue());
          firesByMonth.put(monthKey, firesByMonth.getOrDefault(monthKey, 0L) + 1);
        }
      }
    });
    
    // Формируем результат в хронологическом порядке
    current = startDate.withDayOfMonth(1);
    while (!current.isAfter(endDate)) {
      String monthKey = String.format("%d-%02d", current.getYear(), current.getMonthValue());
      String monthName = monthNames[current.getMonthValue() - 1];
      // Если год отличается от текущего, добавляем год к названию
      String displayName = current.getYear() != now.getYear() 
          ? String.format("%s %d", monthName, current.getYear())
          : monthName;
      
      result.add(new AnalyticsDto.HiresFiresData(
          displayName,
          hiresByMonth.getOrDefault(monthKey, 0L),
          firesByMonth.getOrDefault(monthKey, 0L)
      ));
      current = current.plusMonths(1);
    }
    
    return result;
  }

  /**
   * Вычислить сводку по отделам
   */
  private List<AnalyticsDto.DepartmentSummary> calculateDepartmentsSummary(List<Employee> employees) {
    return departmentRepository.findAll().stream()
        .map(dept -> {
          List<Employee> deptEmployees = employees.stream()
              .filter(e -> e.getDepartment() != null && e.getDepartment().getId().equals(dept.getId()))
              .collect(Collectors.toList());
          
          double avgComp = deptEmployees.stream()
              .filter(e -> e.getCompetenceLevel() != null)
              .mapToInt(Employee::getCompetenceLevel)
              .average()
              .orElse(0.0);
          
          return new AnalyticsDto.DepartmentSummary(
              dept.getName(),
              (long) deptEmployees.size(),
              avgComp
          );
        })
        .collect(Collectors.toList());
  }

  /**
   * Вычислить сводку по сотрудникам
   * 
   * Edge cases:
   * - Обработка null значений employee в trainings и absences
   * - Обработка null значений fullName и competenceLevel
   * - Безопасная обработка отсутствующих данных
   */
  private List<AnalyticsDto.EmployeeSummary> calculateEmployeesSummary(
      List<Employee> employees,
      List<Training> allTrainings,
      List<AbsenceEntity> allAbsences) {
    
    if (employees.isEmpty()) {
      return new ArrayList<>();
    }
    
    // Безопасно собираем ID обученных сотрудников
    Set<Long> trainedEmployeeIds = allTrainings.stream()
        .filter(t -> t != null && t.getEmployee() != null && t.getEmployee().getId() != null)
        .map(t -> t.getEmployee().getId())
        .collect(Collectors.toSet());
    
    // Безопасно собираем количество пропусков по сотрудникам
    Map<Long, Long> absencesByEmployee = allAbsences.stream()
        .filter(a -> a != null && a.getEmployee() != null && a.getEmployee().getId() != null)
        .collect(Collectors.groupingBy(
            a -> a.getEmployee().getId(),
            Collectors.counting()
        ));
    
    return employees.stream()
        .filter(e -> e != null) // Фильтруем null сотрудников
        .map(e -> new AnalyticsDto.EmployeeSummary(
            e.getFullName() != null ? e.getFullName() : "Не указано",
            e.getCompetenceLevel() != null ? e.getCompetenceLevel() : 0,
            trainedEmployeeIds.contains(e.getId()) ? "Да" : "Нет",
            absencesByEmployee.getOrDefault(e.getId(), 0L)
        ))
        .collect(Collectors.toList());
  }
}

