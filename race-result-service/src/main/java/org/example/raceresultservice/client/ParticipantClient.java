package org.example.raceresultservice.client;

import org.example.raceresultservice.model.Driver;
import org.example.raceresultservice.model.Team;
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
    
    // Lấy thông tin tất cả tay đua theo ID
    @GetMapping("/api/drivers/bulk")
    List<Driver> getDriversByIds(@RequestParam Set<String> ids);
    
    // Lấy thông tin đội theo ID
    @GetMapping("/api/teams/{id}")
    Team getTeamById(@PathVariable String id);
    
    // Lấy thông tin tất cả đội theo ID
    @GetMapping("/api/teams/bulk")
    List<Team> getTeamsByIds(@RequestParam Set<String> ids);
} 