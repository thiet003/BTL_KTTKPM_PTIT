package org.example.standings_service.client;

import org.example.standings_service.model.DriverRaceResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Feign client to communicate with race-result-service
 */
@FeignClient(name = "race-result-service")
public interface RaceResultClient {
    // Lấy tất cả kết quả đua xe cho stage đua vừa cập nhật
    @GetMapping("/api/race-results/race-stage/{raceStageId}")
    List<DriverRaceResult> getResultsByRaceStageId(@PathVariable String raceStageId);
    // Lấy tất cả kết quả đua xe cho tay đua trong mùa giải
    @GetMapping("/api/race-results/driver/{driverId}/season/{seasonId}")
    List<DriverRaceResult> getResultsByDriverIdAndSeasonId(@PathVariable String driverId, @PathVariable String seasonId);
}