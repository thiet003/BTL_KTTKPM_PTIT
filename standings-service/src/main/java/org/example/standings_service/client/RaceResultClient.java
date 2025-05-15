package org.example.standings_service.client;

import org.example.standings_service.model.DriverRaceResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * Feign client to communicate with race-result-service
 * Note: Tất cả các tham số phải không được null, nếu không sẽ gây ra lỗi khi gọi API
 */
@FeignClient(name = "race-result-service")
public interface RaceResultClient {
    /**
     * Lấy tất cả kết quả đua xe cho stage đua
     * @param raceStageId ID của chặng đua, không được null
     * @return Danh sách kết quả đua
     */
    @GetMapping("/api/race-results/race-stage/{raceStageId}")
    List<DriverRaceResult> getResultsByRaceStageId(@PathVariable String raceStageId);
    
    /**
     * Lấy tất cả kết quả đua xe cho tay đua trong mùa giải
     * @param driverId ID của tay đua, không được null
     * @param seasonId ID của mùa giải, không được null
     * @return Danh sách kết quả đua
     */
    @GetMapping("/api/race-results/driver/{driverId}/season/{seasonId}")
    List<DriverRaceResult> getResultsByDriverIdAndSeasonId(@PathVariable String driverId, @PathVariable String seasonId);
}