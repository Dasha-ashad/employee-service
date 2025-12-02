package com.daria.repository;

import com.daria.entity.AbsenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AbsenceRepository extends JpaRepository<AbsenceEntity, Long> {
  List<AbsenceEntity> findByEmployeeId(Long employeeId);
}
