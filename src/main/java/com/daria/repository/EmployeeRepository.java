package com.daria.repository;

import com.daria.entity.Employee;
import com.daria.entity.enums.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
  
  /**
   * Поиск сотрудников с фильтрацией
   * 
   * @param search поисковый запрос (поиск по fullName, игнорирует регистр)
   * @param departmentId фильтр по отделу (null = все отделы)
   * @param gender фильтр по полу (null = все)
   * @return список сотрудников, соответствующих критериям
   */
  @Query("SELECT e FROM Employee e WHERE " +
      "(:search IS NULL OR :search = '' OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
      "(:departmentId IS NULL OR e.department.id = :departmentId) AND " +
      "(:gender IS NULL OR e.gender = :gender)")
  List<Employee> findEmployeesWithFilters(
      @Param("search") String search,
      @Param("departmentId") Long departmentId,
      @Param("gender") Gender gender
  );
  
  /**
   * Поиск сотрудников с фильтрацией и сортировкой по количеству пропусков
   * 
   * @param search поисковый запрос
   * @param departmentId фильтр по отделу
   * @param gender фильтр по полу
   * @return список сотрудников, отсортированных по количеству пропусков (по убыванию)
   */
  @Query("SELECT e FROM Employee e " +
      "LEFT JOIN e.absenceEntities a " +
      "WHERE " +
      "(:search IS NULL OR :search = '' OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
      "(:departmentId IS NULL OR e.department.id = :departmentId) AND " +
      "(:gender IS NULL OR e.gender = :gender) " +
      "GROUP BY e.id " +
      "ORDER BY COUNT(a.id) DESC")
  List<Employee> findEmployeesWithFiltersSortedByAbsences(
      @Param("search") String search,
      @Param("departmentId") Long departmentId,
      @Param("gender") Gender gender
  );
  
  /**
   * Поиск сотрудников с фильтрацией и сортировкой по уровню компетенции
   * 
   * @param search поисковый запрос
   * @param departmentId фильтр по отделу
   * @param gender фильтр по полу
   * @return список сотрудников, отсортированных по уровню компетенции (по убыванию)
   */
  @Query("SELECT e FROM Employee e WHERE " +
      "(:search IS NULL OR :search = '' OR LOWER(e.fullName) LIKE LOWER(CONCAT('%', :search, '%'))) AND " +
      "(:departmentId IS NULL OR e.department.id = :departmentId) AND " +
      "(:gender IS NULL OR e.gender = :gender) " +
      "ORDER BY COALESCE(e.competenceLevel, 0) DESC")
  List<Employee> findEmployeesWithFiltersSortedByCompetence(
      @Param("search") String search,
      @Param("departmentId") Long departmentId,
      @Param("gender") Gender gender
  );
}