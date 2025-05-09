package org.example.standings_service.model;

import lombok.Data;

/**
 * Model class for consuming race results from race-result-service
 */
@Data
public class DriverRaceResult {
    private String id;
    private String raceStageId;
    private String driverId;
    private String teamId;
    private String driverName;
    private String teamName;
    private String seasonId;
    private Integer gridPosition;
    private Integer finishPosition;
    private Integer points;
    private String status;
    private String finishTimeOrGap;
    private Integer lapsCompleted;
}
