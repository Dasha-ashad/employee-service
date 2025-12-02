package com.daria.infra;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "app.jwt")
@Validated
@Getter
@Setter
public class JwtProperties {

  @NotBlank(message = "JWT secret key must not be blank")
  private String secret;

  @Positive(message = "JWT expiration time must be positive")
  private long expirationMs = 3600000; // 1 hour default

}

