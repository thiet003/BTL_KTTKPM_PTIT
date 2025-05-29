package org.example.standings_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.time.LocalDate;

/**
 * Model class for consuming race results from race-result-service
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DriverRaceResult {
    private String id;
    
    @JsonIgnoreProperties({"season"})
    private RaceStageInfo raceStage;
    
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private DriverTeamAssignmentInfo driverTeamAssignment;
    
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
        return driverTeamAssignment != null ? driverTeamAssignment.getDriver() : null;
    }
    
    public String getDriverId() {
        DriverInfo driver = getDriver();
        return driver != null ? driver.getId() : null;
    }
    
    public TeamInfo getTeam() {
        return driverTeamAssignment != null ? driverTeamAssignment.getTeam() : null;
    }
    
    public String getTeamId() {
        TeamInfo team = getTeam();
        return team != null ? team.getId() : null;
    }
    
    public String getSeasonId() {
        return season != null ? season.getId() : null;
    }
    
    public String getDriverName() {
        DriverInfo driver = getDriver();
        return driver != null ? driver.getFullName() : null;
    }
    
    public String getTeamName() {
        TeamInfo team = getTeam();
        return team != null ? team.getName() : null;
    }
    
    public DriverTeamAssignmentInfo getDriverTeamAssignment() {
        return driverTeamAssignment;
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
    public static class DriverTeamAssignmentInfo {
        private String id;
        
        @JsonIgnoreProperties({"teamAssignments", "hibernateLazyInitializer", "handler"})
        private DriverInfo driver;
        
        @JsonIgnoreProperties({"driverAssignments", "hibernateLazyInitializer", "handler"})
        private TeamInfo team;
        
        private LocalDate startDate;
        private LocalDate endDate;
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
