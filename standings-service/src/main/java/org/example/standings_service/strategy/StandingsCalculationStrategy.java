package org.example.standings_service.strategy;

import org.example.standings_service.model.Driver;
import org.example.standings_service.model.DriverStanding;
import org.example.standings_service.model.RacingTeam;
import org.example.standings_service.model.Season;

import java.util.List;

/**
 * Strategy interface để tính toán standings
 */
public interface StandingsCalculationStrategy {
    
    /**
     * Tính toán driver standing cho một driver trong một season
     */
    DriverStanding calculateDriverStanding(String seasonId, String driverId,
                                         String teamId, List<Integer> racePoints);
    
    /**
     * Tính toán driver standing với entity objects
     */
    default DriverStanding calculateDriverStanding(Season season, Driver driver,
                                                 RacingTeam team, List<Integer> racePoints) {
        return calculateDriverStanding(
                season.getId(),
                driver.getId(),
                team != null ? team.getId() : null,
                racePoints
        );
    }
}