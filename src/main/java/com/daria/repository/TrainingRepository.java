package com.daria.repository;

import com.daria.entity.Training;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {
  List<Training> findByEmployeeId(Long employeeId);
}