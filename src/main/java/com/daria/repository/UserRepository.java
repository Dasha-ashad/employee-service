package com.daria.repository;

import com.daria.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByLogin(String login);
  
  default Optional<User> findByUsername(String username) {
    return findByLogin(username);
  }
}
