package org.example.standings_service.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model class for representing race stages in standings service
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceStage {
    private String id;
    private String name;
    private Season season;
    private String circuitId;
    private String circuitName;
    private String raceDate;
    private Integer laps;
    private String status;
    
    // Helper method to get seasonId
    public String getSeasonId() {
        return season != null ? season.getId() : null;
    }
} 