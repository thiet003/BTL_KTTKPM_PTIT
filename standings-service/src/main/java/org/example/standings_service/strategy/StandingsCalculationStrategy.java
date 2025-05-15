package org.example.standings_service.strategy;


import org.example.standings_service.model.Driver;
import org.example.standings_service.model.DriverRaceResult;
import org.example.standings_service.model.DriverStanding;
import org.example.standings_service.model.RacingTeam;
import org.example.standings_service.model.Season;

import java.util.List;

/**
 * Strategy pattern for calculating driver standings
 */
public interface StandingsCalculationStrategy {
    // Tính toán xếp hạng cho tay đua mới (using ID references)
    DriverStanding calculateDriverStanding(String seasonId, String driverId, String driverName, 
                                          String teamId, String teamName, String nationality, 
                                          List<DriverRaceResult> results);
    
    // Tính toán xếp hạng cho tay đua mới (using entity references)
    default DriverStanding calculateDriverStanding(Season season, Driver driver, String driverName, 
                                                  RacingTeam team, String teamName, String nationality, 
                                                  List<DriverRaceResult> results) {
        return calculateDriverStanding(
                season.getId(), 
                driver.getId(), 
                driverName, 
                team != null ? team.getId() : null, 
                teamName, 
                nationality, 
                results);
    }
}