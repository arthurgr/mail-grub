package com.mailgrub.security;

import com.mailgrub.security.filter.TokenAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final TokenAuthenticationFilter tokenFilter;

  public SecurityConfig(TokenAuthenticationFilter tokenFilter) {
    this.tokenFilter = tokenFilter;
  }

  @Bean
  SecurityFilterChain api(HttpSecurity http) throws Exception {
    return http.csrf(csrf -> csrf.disable())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .cors(Customizer.withDefaults())
        .authorizeHttpRequests(
            auth ->
                auth
                    // allow CORS preflight
                    .requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    // your public endpoints
                    .requestMatchers("/public/**", "/auth/**", "/actuator/health", "/error")
                    .permitAll()
                    // everything else requires a valid Bearer token
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            ex -> ex.authenticationEntryPoint((req, res, e) -> res.sendError(401, "Unauthorized")))
        .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }
}
