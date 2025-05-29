package org.example.raceresultservice.controller;

import org.example.raceresultservice.model.Driver;
import org.example.raceresultservice.model.DriverRaceResult;
import org.example.raceresultservice.model.DriverTeamAssignment;
import org.example.raceresultservice.model.RaceStage;
import org.example.raceresultservice.model.Season;
import org.example.raceresultservice.service.DriverRaceResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/race-results")
public class DriverRaceResultController {

    @Autowired
    private DriverRaceResultService driverRaceResultService;

    @GetMapping
    // Lấy tất cả kết quả đua
    public ResponseEntity<List<DriverRaceResult>> getAllDriverRaceResults() {
        return ResponseEntity.ok(driverRaceResultService.getAllDriverRaceResults());
    }

    @PostMapping("/race-stage")
    // Lấy kết quả đua theo RaceStage
    public ResponseEntity<List<DriverRaceResult>> getDriverRaceResultsByRaceStage(@RequestBody RaceStage raceStage) {
        return ResponseEntity.ok(driverRaceResultService.getDriverRaceResultsByRaceStage(raceStage));
    }

    @GetMapping("/race-stage/{raceStageId}")
    // Lấy kết quả đua theo id stage đua
    public ResponseEntity<List<DriverRaceResult>> getDriverRaceResultsByRaceStageId(@PathVariable String raceStageId) {
        RaceStage raceStage = new RaceStage();
        raceStage.setId(raceStageId);
        return ResponseEntity.ok(driverRaceResultService.getDriverRaceResultsByRaceStage(raceStage));
    }

    @GetMapping("/driver/{driverId}/season/{seasonId}")
    // Lấy tất cả kết quả đua theo id tay đua và id mùa giải
    public ResponseEntity<List<DriverRaceResult>> getDriverRaceResultsByDriverIdAndSeasonId(
            @PathVariable String driverId, @PathVariable String seasonId) {
        
        return ResponseEntity.ok(driverRaceResultService.getDriverRaceResultsByDriverIdAndSeasonId(driverId, seasonId));
    }
    
    // Lấy kết quả đua theo tay đua và mùa giải(theo hướng đối tượng)
    @GetMapping("/driver/season")
    public ResponseEntity<List<DriverRaceResult>> getDriverRaceResultsByDriverAndSeason(
            @RequestBody Map<String, Object> request) {
        Map<String, Object> driverData = (Map<String, Object>) request.get("driver");
        Map<String, Object> seasonData = (Map<String, Object>) request.get("season");

        Driver driver = new Driver();
        driver.setId((String) driverData.get("id"));

        Season season = new Season();
        season.setId((String) seasonData.get("id"));

        return ResponseEntity.ok(driverRaceResultService.getDriverRaceResultsByDriverAndSeason(driver, season));
    }

    // Cập nhật kết quả chặng đua - phương thức mới tuân thủ nguyên tắc hướng đối tượng
    @PostMapping("/update")
    @Transactional
    public ResponseEntity<List<DriverRaceResult>> updateRaceResults(
            @RequestBody List<DriverRaceResult> results) {
        return ResponseEntity.ok(driverRaceResultService.updateRaceResults(results));
    }
}