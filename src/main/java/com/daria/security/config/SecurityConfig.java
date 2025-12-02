package com.daria.security.config;

import com.daria.security.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Конфигурация безопасности Spring Security
 * 
 * Лучшие практики:
 * - CORS настроен для всех хостов (для development)
 * - JWT аутентификация через фильтр
 * - Stateless сессии
 * - CSRF отключен (так как используется JWT)
 * - Метод-уровневая безопасность включена для использования @PreAuthorize
 */
@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true) // Включаем поддержку @PreAuthorize и @PostAuthorize
public class SecurityConfig {

  private final JwtAuthFilter jwtAuthFilter;

  /**
   * Конфигурация CORS для разрешения запросов со всех источников
   * 
   * Настройка Access-Control-Allow-Origin: * для всех хостов
   * 
   * ВАЖНО: В production рекомендуется ограничить allowedOrigins
   * конкретными доменами вместо "*"
   */
  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    
    // Разрешаем все источники (Access-Control-Allow-Origin: *)
    // Используем List.of("*") для установки заголовка Access-Control-Allow-Origin: *
    configuration.setAllowedOrigins(List.of("*"));
    
    // Разрешаем все методы HTTP
    configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
    
    // Разрешаем все заголовки
    configuration.setAllowedHeaders(List.of("*"));
    
    // При использовании allowedOrigins("*") нельзя использовать allowCredentials(true)
    // Если нужны credentials, используйте setAllowedOriginPatterns("*") вместо setAllowedOrigins
    configuration.setAllowCredentials(false);
    
    // Время кеширования preflight запросов (1 час)
    configuration.setMaxAge(3600L);
    
    // Разрешаем все заголовки в ответе
    configuration.setExposedHeaders(List.of("*"));
    
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  /**
   * Конфигурация цепочки фильтров безопасности
   * 
   * Логика доступа:
   * 1. Публичные endpoints (permitAll):
   *    - /auth/login - вход в систему (доступен всем)
   *    - /swagger-ui/** - документация API (доступна всем)
   * 
   * 2. Защищенные endpoints (требуют аутентификации):
   *    - Все остальные endpoints требуют JWT токен
   *    - Детальный контроль доступа по ролям через @PreAuthorize на методах контроллеров
   * 
   * 3. Роли в системе:
   *    - ROLE_ADMIN - полный доступ ко всем операциям
   *    - ROLE_HEAD - доступ к управлению сотрудниками, обучениями, пропусками
   *    - ROLE_EMPLOYEE - только просмотр данных
   * 
   * 4. Метод-уровневая безопасность:
   *    - @PreAuthorize("hasRole('ADMIN')") - только администраторы
   *    - @PreAuthorize("hasAnyRole('ADMIN', 'HEAD')") - администраторы и руководители
   *    - Без аннотации - все аутентифицированные пользователи
   * 
   * ВАЖНО: Первый администратор создается через миграцию БД (V2__initial_data.sql),
   * поэтому /auth/register требует роль ADMIN (контролируется через @PreAuthorize).
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable)
        // Включаем CORS с нашей конфигурацией
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            // Публичные endpoints (не требуют аутентификации)
            // /auth/login - доступен всем для входа в систему
            // /auth/register - доступен всем на уровне URL, но требует роль ADMIN через @PreAuthorize
            // Это позволяет @PreAuthorize работать корректно: неаутентифицированные получат 403,
            // аутентифицированные без роли ADMIN тоже получат 403, только ADMIN сможет зарегистрировать
            .requestMatchers(
                "/v1/employee-service/auth/login",
                "/v1/employee-service/auth/register"
            ).permitAll()
            // Swagger UI доступен без аутентификации (для удобства разработки)
            .requestMatchers(
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/v3/api-docs/**",
                "/v3/api-docs",
                "/swagger-resources/**",
                "/webjars/**",
                "/swagger-ui/index.html"
            ).permitAll()
            // Административные endpoints требуют роль ADMIN (на уровне URL)
            .requestMatchers("/v1/employee-service/admin/**").hasRole("ADMIN")
            // Все остальные endpoints требуют аутентификации
            // Детальный контроль доступа по ролям выполняется через @PreAuthorize на методах
            .anyRequest().authenticated()
        );

    // Добавляем JWT фильтр перед стандартным фильтром аутентификации
    // Фильтр извлекает токен из заголовка Authorization, валидирует его
    // и устанавливает аутентификацию в SecurityContext
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