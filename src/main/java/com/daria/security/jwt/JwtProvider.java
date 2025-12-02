package com.daria.security.jwt;

import com.daria.infra.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {

  private final JwtProperties jwtProperties;
  private final SecretKey secretKey;

  public JwtProvider(JwtProperties jwtProperties) {
    this.jwtProperties = jwtProperties;
    this.secretKey = Keys.hmacShaKeyFor(
        jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8)
    );
  }

  public String generateAccessToken(String username, String role) {
    return Jwts.builder()
        .subject(username)
        .claim("role", role)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationMs()))
        .signWith(secretKey)
        .compact();
  }

  public String getUsername(String token) {
    return getClaims(token).getSubject();
  }

  public String getRole(String token) {
    return getClaims(token).get("role", String.class);
  }

  public boolean isValid(String token) {
    try {
      getClaims(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  private Claims getClaims(String token) {
    return Jwts.parser()
        .verifyWith(secretKey)
        .build()
        .parseSignedClaims(token)
        .getPayload();
  }
}

