package com.daria.controller;

import com.daria.dto.AuthResponse;
import com.daria.dto.LoginRequest;
import com.daria.dto.RegisterRequest;
import com.daria.security.jwt.JwtProvider;
import com.daria.security.service.CustomUserDetailsService;
import com.daria.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/employee-service/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "API для аутентификации и регистрации пользователей")
public class AuthController {

  private final AuthService authService;
  private final AuthenticationManager authManager;
  private final JwtProvider jwtProvider;
  private final CustomUserDetailsService userDetailsService;

  @Operation(
      summary = "Регистрация нового пользователя",
      description = "Создает нового пользователя в системе. Доступно только для администраторов."
  )
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
      @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
      @ApiResponse(responseCode = "409", description = "Пользователь с таким логином уже существует"),
      @ApiResponse(responseCode = "403", description = "Доступ запрещен. Требуется роль ADMIN")
  })
  @PostMapping("/register")
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('ADMIN')") // Только администраторы могут регистрировать новых пользователей
  public void register(@RequestBody RegisterRequest req) {
    authService.register(req);
  }

  @Operation(
      summary = "Вход в систему",
      description = "Аутентифицирует пользователя и возвращает JWT токен для доступа к защищенным endpoints"
  )
  @ApiResponses(value = {
      @ApiResponse(
          responseCode = "200",
          description = "Успешная аутентификация",
          content = @Content(schema = @Schema(implementation = AuthResponse.class))
      ),
      @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
  })
  @PostMapping("/login")
  public AuthResponse login(@RequestBody LoginRequest req) {

    authManager.authenticate(
        new UsernamePasswordAuthenticationToken(req.username(), req.password())
    );

    var user = userDetailsService.loadUserByUsername(req.username());

    String token = jwtProvider.generateAccessToken(
        user.getUsername(),
        user.getAuthorities().iterator().next().getAuthority()
    );

    return new AuthResponse(token);
  }
}