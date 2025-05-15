package org.example.standings_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * Model class for consuming race results from race-result-service
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverRaceResult {
    private String id;
    
    @JsonIgnoreProperties({"season"})
    private RaceStageInfo raceStage;
    
    @JsonIgnoreProperties({"driverStandings"})
    private DriverInfo driver;
    
    @JsonIgnoreProperties({"driverStandings"})
    private TeamInfo team;
    
    @JsonIgnoreProperties({"driverStandings"})
    private SeasonInfo season;
    
    private Integer gridPosition;
    private Integer finishPosition;
    private Integer points;
    private String status;
    private String finishTimeOrGap;
    private Integer lapsCompleted;
    
    // Phương thức trợ giúp
    public String getRaceStageId() {
        return raceStage != null ? raceStage.getId() : null;
    }
    
    public RaceStageInfo getRaceStage() {
        return raceStage;
    }
    
    public DriverInfo getDriver() {
        return driver;
    }
    
    public String getDriverId() {
        return driver != null ? driver.getId() : null;
    }
    
    public String getTeamId() {
        return team != null ? team.getId() : null;
    }
    
    public String getSeasonId() {
        return season != null ? season.getId() : null;
    }
    
    public String getDriverName() {
        return driver != null ? driver.getFullName() : null;
    }
    
    public String getTeamName() {
        return team != null ? team.getName() : null;
    }
    
    // Lớp nội bộ để ánh xạ
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RaceStageInfo {
        private String id;
        private String name;
        private String raceDate;
        private Integer laps;
        private String circuitName;
        private SeasonInfo season;
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DriverInfo {
        private String id;
        private String fullName;
        private String nationality;
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class TeamInfo {
        private String id;
        private String name;
        private String country;
        private String base;
    }
    
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SeasonInfo {
        private String id;
        private String name;
    }
}
