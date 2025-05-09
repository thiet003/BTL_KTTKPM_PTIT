package org.example.standings_service.controller;

import org.example.standings_service.model.DriverStanding;
import org.example.standings_service.service.DriverStandingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/driver-standings")
public class DriverStandingController {

    @Autowired
    private DriverStandingService driverStandingService;
    // Lấy tất cả xếp hạng tay đua
    @GetMapping
    public ResponseEntity<List<DriverStanding>> getAllDriverStandings() {
        return ResponseEntity.ok(driverStandingService.getAllDriverStandings());
    }
    // Lấy xếp hạng tay đua theo mùa giải
    @GetMapping("/season/{seasonId}")
    public ResponseEntity<List<DriverStanding>> getDriverStandingsBySeasonId(@PathVariable String seasonId) {
        return ResponseEntity.ok(driverStandingService.getDriverStandingsBySeasonId(seasonId));
    }
    // Lấy xếp hạng tay đua theo mùa giải và id tay đua
    @GetMapping("/season/{seasonId}/driver/{driverId}")
    public ResponseEntity<DriverStanding> getDriverStandingBySeasonIdAndDriverId(
            @PathVariable String seasonId, @PathVariable String driverId) {
        return driverStandingService.getDriverStandingBySeasonIdAndDriverId(seasonId, driverId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // Tính toán lại xếp hạng cho mùa giải
    @PostMapping("/recalculate")
    public ResponseEntity<Void> recalculateStandings(@RequestParam String seasonId) {
        driverStandingService.recalculateStandingsForSeason(seasonId);
        return ResponseEntity.ok().build();
    }
}