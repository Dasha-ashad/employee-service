package com.daria.security.config;

import com.daria.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable)
        .cors(CorsConfigurer::disable)
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/v1/employee-service/auth/register",
                "/v1/employee-service/auth/login"
            ).permitAll()
            .requestMatchers(
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/v3/api-docs",
                "/swagger-resources/**",
                "/webjars/**",
                "/swagger-ui/index.html"
            ).permitAll()
            .requestMatchers("/v1/employee-service/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated()
        );

    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

  @Bean
  public BCryptPasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authManager(AuthenticationConfiguration configuration) throws Exception {
    return configuration.getAuthenticationManager();
  }
}