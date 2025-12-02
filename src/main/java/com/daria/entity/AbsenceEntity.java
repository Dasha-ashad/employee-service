package com.daria.entity;

import com.daria.entity.enums.AbsenceStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "absences")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AbsenceEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "employee_id", nullable = false)
  private Employee employee;

  @Column(nullable = false)
  private LocalDate startDate;

  private LocalDate endDate;

  private String description;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  @Builder.Default
  private AbsenceStatus status = AbsenceStatus.GOOD_REASON;
}