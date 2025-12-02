package com.daria.entity;

import com.daria.entity.enums.CompetenceRank;
import com.daria.entity.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

  @Column(name = "full_name", nullable = false)
  private String fullName;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Gender gender;

  private LocalDate birthDate;

  @Column(nullable = false)
  private LocalDate hireDate;

  private LocalDate fireDate;

  @Enumerated(EnumType.STRING)
  @Column(name = "competence_rank", nullable = false)
  private CompetenceRank competenceRank;

  @Column(name = "competence_level")
  private Integer competenceLevel;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "department_id")
  private Department department;

  @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
  private List<Training> trainings;

  @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
  private List<AbsenceEntity> absenceEntities;
}
