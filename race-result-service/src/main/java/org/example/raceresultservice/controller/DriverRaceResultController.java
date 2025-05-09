package org.example.raceresultservice.controller;

import org.example.raceresultservice.model.DriverRaceResult;
import org.example.raceresultservice.service.DriverRaceResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/{id}")
    // Lấy kết quả đua theo id
    public ResponseEntity<DriverRaceResult> getDriverRaceResultById(@PathVariable String id) {
        return driverRaceResultService.getDriverRaceResultById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/race-stage/{raceStageId}")
    // Lấy kết quả đua theo id stage đua
    public ResponseEntity<List<DriverRaceResult>> getDriverRaceResultsByRaceStageId(@PathVariable String raceStageId) {
        return ResponseEntity.ok(driverRaceResultService.getDriverRaceResultsByRaceStageId(raceStageId));
    }

    @GetMapping("/driver/{driverId}")
    // Lấy kết quả đua theo id tay đua
    public ResponseEntity<List<DriverRaceResult>> getDriverRaceResultsByDriverId(@PathVariable String driverId) {
        return ResponseEntity.ok(driverRaceResultService.getDriverRaceResultsByDriverId(driverId));
    }

    @GetMapping("/driver/{driverId}/season/{seasonId}")
    // Lấy tất cả kết quả đua theo id tay đua và id mùa giải
    public ResponseEntity<List<DriverRaceResult>> getDriverRaceResultsByDriverIdAndSeasonId(
            @PathVariable String driverId, @PathVariable String seasonId) {
        return ResponseEntity.ok(driverRaceResultService.getDriverRaceResultsByDriverIdAndSeasonId(driverId, seasonId));
    }

    // Cập nhật kết quả chặng đua
    @PostMapping("/update/{raceStageId}")
    public ResponseEntity<List<DriverRaceResult>> updateRaceResultsAlternative(
            @PathVariable String raceStageId,
            @RequestBody List<DriverRaceResult> results,
            @RequestParam String seasonId) {

        return ResponseEntity.ok(driverRaceResultService.updateRaceResults(raceStageId, results, seasonId));
    }
}