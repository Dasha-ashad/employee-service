package com.daria.entity;

import com.daria.entity.enums.CompetenceRank;
import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;

@Entity
@Table(name = "trainings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Training {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;

  @Column(name = "training_name", nullable = false)
  private String trainingName;

  private LocalDate startDate;
  private LocalDate endDate;

  @Enumerated(EnumType.STRING)
  private CompetenceRank levelBefore;

  @Enumerated(EnumType.STRING)
  private CompetenceRank levelAfter;
}
