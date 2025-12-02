package com.daria.repository;

import com.daria.entity.AbsenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AbsenceRepository extends JpaRepository<AbsenceEntity, Long> {
  List<AbsenceEntity> findByEmployeeId(Long employeeId);
  
  /**
   * Найти все пропуски, отсортированные по дате начала (от новых к старым)
   */
  @Query("SELECT a FROM AbsenceEntity a ORDER BY a.startDate DESC")
  List<AbsenceEntity> findAllOrderByStartDateDesc();
  
  /**
   * Найти пропуски сотрудника, отсортированные по дате начала
   */
  @Query("SELECT a FROM AbsenceEntity a WHERE a.employee.id = :employeeId ORDER BY a.startDate DESC")
  List<AbsenceEntity> findByEmployeeIdOrderByStartDateDesc(@Param("employeeId") Long employeeId);
}
