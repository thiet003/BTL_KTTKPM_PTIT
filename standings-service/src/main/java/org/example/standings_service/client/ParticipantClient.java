package org.example.standings_service.client;

import org.example.standings_service.model.Driver;
import org.example.standings_service.model.RacingTeam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Set;

/**
 * Feign client to communicate with participant-service
 */
@FeignClient(name = "participant-service")
public interface ParticipantClient {
    // Lấy thông tin tay đua theo ID
    @GetMapping("/api/drivers/{id}")
    Driver getDriverById(@PathVariable String id);
    // Lấy thông tin đội theo ID
    @GetMapping("/api/teams/{id}")
    RacingTeam getTeamById(@PathVariable String id);
}