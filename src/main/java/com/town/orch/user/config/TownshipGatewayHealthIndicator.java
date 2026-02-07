package com.town.orch.user.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.ReactiveHealthIndicator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class TownshipGatewayHealthIndicator implements ReactiveHealthIndicator {

  private final WebClient webClient;

  public TownshipGatewayHealthIndicator(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder.build();
  }


  @Override
  public Mono<Health> health() {
    // Zip combines multiple Monos and waits for all of them to complete
    return Mono.zip(
            checkDownstreamService("Auth-Service", "http://auth-service/actuator/health"),
            checkDownstreamService("User-Service", "http://user-service/actuator/health"),
            checkMemoryUsage())
        .map(
            tuple -> {
              boolean allUp = tuple.getT1() && tuple.getT2() && tuple.getT3();

              Health.Builder status = allUp ? Health.up() : Health.down();
              return status
                  .withDetail("authService", tuple.getT1())
                  .withDetail("userService", tuple.getT2())
                  .withDetail("memoryHealthy", tuple.getT3())
                  .build();
            });
  }

  /**
   * Use Case 1: Checking Downstream Microservices Logic: Hits the actuator of a child service.
   * Returns false if timeout or error.
   */
  private Mono<Boolean> checkDownstreamService(String name, String url) {
    return webClient
        .get()
        .uri(url)
        .retrieve()
        .toBodilessEntity()
        .timeout(Duration.ofSeconds(2)) // Critical: Don't let health check hang
        .map(response -> response.getStatusCode().is2xxSuccessful())
        .onErrorReturn(false); // If service is down, this check is false
  }

  /**
   * Use Case 2: System Resource Check (Memory) Logic: Checks if the JVM has enough memory to
   * operate.
   */
  private Mono<Boolean> checkMemoryUsage() {
    long freeMemory = Runtime.getRuntime().freeMemory();
    long totalMemory = Runtime.getRuntime().totalMemory();
    double freePercent = (double) freeMemory / totalMemory;

    // Return true if more than 10% memory is free
    return Mono.just(freePercent > 0.10);
  }
}
