package com.daria.security.filter;

import com.daria.security.jwt.JwtProvider;
import com.daria.security.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private final JwtProvider jwtProvider;
  private final CustomUserDetailsService userDetailsService;

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain)
      throws ServletException, IOException {

    String header = request.getHeader("Authorization");

    if (header != null && header.startsWith("Bearer ")) {
      String token = header.substring(7);

      if (jwtProvider.isValid(token)) {
        String username = jwtProvider.getUsername(token);

        var userDetails = userDetailsService.loadUserByUsername(username);

        var auth = new UsernamePasswordAuthenticationToken(
            userDetails,
            null,
            userDetails.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    }

    filterChain.doFilter(request, response);
  }
}