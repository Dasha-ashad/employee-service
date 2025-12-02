package com.daria.service;

import com.daria.dto.TrainingCreateRequest;
import com.daria.dto.TrainingDto;
import com.daria.dto.TrainingUpdateRequest;
import com.daria.entity.Employee;
import com.daria.entity.Training;
import com.daria.exception.ResourceNotFoundException;
import com.daria.repository.EmployeeRepository;
import com.daria.repository.TrainingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TrainingService {

  private final TrainingRepository trainingRepository;
  private final EmployeeRepository employeeRepository;

  public List<TrainingDto> getAllTrainings() {
    return trainingRepository.findAll().stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public TrainingDto getTrainingById(Long id) {
    Training training = trainingRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Training", id));
    return toDto(training);
  }

  public List<TrainingDto> getTrainingsByEmployeeId(Long employeeId) {
    return trainingRepository.findByEmployeeId(employeeId).stream()
        .map(this::toDto)
        .collect(Collectors.toList());
  }

  public TrainingDto createTraining(TrainingCreateRequest request) {
    Employee employee = employeeRepository.findById(request.employeeId())
        .orElseThrow(() -> new ResourceNotFoundException("Employee", request.employeeId()));

    Training training = Training.builder()
        .employee(employee)
        .trainingName(request.trainingName())
        .startDate(request.startDate())
        .endDate(request.endDate())
        .levelBefore(request.levelBefore())
        .levelAfter(request.levelAfter())
        .build();

    Training saved = trainingRepository.save(training);
    return toDto(saved);
  }

  public TrainingDto updateTraining(Long id, TrainingUpdateRequest request) {
    Training training = trainingRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Training", id));

    if (request.trainingName() != null) {
      training.setTrainingName(request.trainingName());
    }
    if (request.startDate() != null) {
      training.setStartDate(request.startDate());
    }
    if (request.endDate() != null) {
      training.setEndDate(request.endDate());
    }
    if (request.levelBefore() != null) {
      training.setLevelBefore(request.levelBefore());
    }
    if (request.levelAfter() != null) {
      training.setLevelAfter(request.levelAfter());
    }

    Training updated = trainingRepository.save(training);
    return toDto(updated);
  }

  public void deleteTraining(Long id) {
    Training training = trainingRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Training", id));
    trainingRepository.delete(training);
  }

  private TrainingDto toDto(Training training) {
    return TrainingDto.of(
        training.getId(),
        training.getEmployee().getId(),
        training.getTrainingName(),
        training.getStartDate(),
        training.getEndDate(),
        training.getLevelBefore(),
        training.getLevelAfter()
    );
  }
}

