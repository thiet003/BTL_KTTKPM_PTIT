package org.example.raceresultservice.strategy;

import org.example.raceresultservice.model.DriverRaceResult;

/**
 * Strategy pattern cho việc tính điểm
 */
public interface PointsCalculationStrategy {
    int calculatePoints(DriverRaceResult result);
}