package org.example.apigateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Gateway configuration implementing the Facade pattern
 * The Facade pattern provides a unified interface to the set of microservices
 */
@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                // Race Schedule Service Routes
                .route("race-schedule-service", r -> r
                        .path("/api/seasons/**", "/api/circuits/**", "/api/race-stages/**")
                        .uri("lb://race-schedule-service"))

                // Participant Service Routes
                .route("participant-service", r -> r
                        .path("/api/drivers/**", "/api/teams/**", "/api/driver-assignments/**")
                        .uri("lb://participant-service"))

                // Race Result Service Routes
                .route("race-result-service", r -> r
                        .path("/api/race-entries/**", "/api/race-results/**")
                        .uri("lb://race-result-service"))

                // Standings Service Routes
                .route("standings-service", r -> r
                        .path("/api/driver-standings/**")
                        .uri("lb://standings-service"))
                .build();
    }
}