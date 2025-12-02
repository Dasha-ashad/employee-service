package com.daria.security.service;

import com.daria.entity.User;
import com.daria.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String login)
      throws UsernameNotFoundException {

    User user = repository.findByUsername(login)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));


    return new org.springframework.security.core.userdetails.User(
        user.getLogin(),
        user.getPasswordHash(),
        java.util.List.of(new SimpleGrantedAuthority(user.getRole().getRoleName()))
    );
  }
}