package org.example.standings_service.strategy;


import org.example.standings_service.model.DriverRaceResult;
import org.example.standings_service.model.DriverStanding;

import java.util.List;

/**
 * Strategy pattern for calculating driver standings
 */
public interface StandingsCalculationStrategy {
    // Tính toán xếp hạng cho tay đua mới
    DriverStanding calculateDriverStanding(String seasonId, String driverId, String driverName, String teamId, String teamName, String nationality, List<DriverRaceResult> results);
}