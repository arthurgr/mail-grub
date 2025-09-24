package com.mailgrub.config;

import java.time.Duration;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.ratelimit")
public class AppRateLimitProperties {
  private boolean enabled = true;
  private int capacity = 100;
  private int refillTokens = 100;
  private Duration refillPeriod = Duration.ofMinutes(1);
  private String keyStrategy = "ip"; // ip or user
  private List<String> excludePaths =
      List.of("/actuator/health", "/v3/api-docs/**", "/swagger-ui/**");

  // getters/setters
  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public int getCapacity() {
    return capacity;
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public int getRefillTokens() {
    return refillTokens;
  }

  public void setRefillTokens(int refillTokens) {
    this.refillTokens = refillTokens;
  }

  public Duration getRefillPeriod() {
    return refillPeriod;
  }

  public void setRefillPeriod(Duration refillPeriod) {
    this.refillPeriod = refillPeriod;
  }

  public String getKeyStrategy() {
    return keyStrategy;
  }

  public void setKeyStrategy(String keyStrategy) {
    this.keyStrategy = keyStrategy;
  }

  public List<String> getExcludePaths() {
    return excludePaths;
  }

  public void setExcludePaths(List<String> excludePaths) {
    this.excludePaths = excludePaths;
  }
}
