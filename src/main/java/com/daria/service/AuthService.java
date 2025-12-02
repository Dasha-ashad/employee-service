package com.daria.service;

import com.daria.dto.RegisterRequest;
import com.daria.entity.User;
import com.daria.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder encoder;

  public void register(RegisterRequest req) {
    if (userRepository.findByUsername(req.login()).isPresent()) {
      throw new com.daria.exception.ConflictException("User with login '" + req.login() + "' already exists");
    }

    User user = User.builder()
        .login(req.login())
        .passwordHash(encoder.encode(req.password()))
        .role(req.role())
        .build();

    userRepository.save(user);
  }
}