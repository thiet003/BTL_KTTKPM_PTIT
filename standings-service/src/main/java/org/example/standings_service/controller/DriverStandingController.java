package org.example.standings_service.controller;

import org.example.standings_service.model.Driver;
import org.example.standings_service.model.DriverStanding;
import org.example.standings_service.model.Season;
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
    
    // Lấy xếp hạng tay đua theo mùa giải (sử dụng ID)
    @GetMapping("/season/{seasonId}")
    public ResponseEntity<List<DriverStanding>> getDriverStandingsBySeasonId(@PathVariable String seasonId) {
        Season season = new Season();
        season.setId(seasonId);
        return ResponseEntity.ok(driverStandingService.getDriverStandingsBySeason(season));
    }
    
    // Lấy xếp hạng tay đua theo đối tượng mùa giải
    @PostMapping("/season")
    public ResponseEntity<List<DriverStanding>> getDriverStandingsBySeason(@RequestBody Season season) {
        return ResponseEntity.ok(driverStandingService.getDriverStandingsBySeason(season));
    }
    
    // Lấy xếp hạng tay đua theo mùa giải và id tay đua
    @GetMapping("/season/{seasonId}/driver/{driverId}")
    public ResponseEntity<DriverStanding> getDriverStandingBySeasonIdAndDriverId(
            @PathVariable String seasonId, @PathVariable String driverId) {
        Season season = new Season();
        season.setId(seasonId);
        Driver driver = new Driver();
        driver.setId(driverId);
        return driverStandingService.getDriverStandingBySeasonAndDriver(season, driver)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Lấy xếp hạng tay đua theo đối tượng mùa giải và tay đua
    @PostMapping("/season-driver")
    public ResponseEntity<DriverStanding> getDriverStandingBySeasonAndDriver(
            @RequestBody Season season, @RequestBody Driver driver) {
        return driverStandingService.getDriverStandingBySeasonAndDriver(season, driver)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    // Tính toán lại xếp hạng cho mùa giải (sử dụng đối tượng)
    @PostMapping("/recalculate/season")
    public ResponseEntity<Void> recalculateStandings(@RequestBody Season season) {
        driverStandingService.recalculateStandingsForSeason(season);
        return ResponseEntity.ok().build();
    }
}