package com.daria.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "departments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Department {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 100)
  private String name;

  // Руководитель отдела
  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "head_id")
  private Employee head;

  @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
  private List<Employee> employees;
}
