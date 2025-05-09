package org.example.raceresultservice.model;

import lombok.Data;

/**
 * Model class for consuming team data from participant-service
 */
@Data
public class RacingTeam {
    private String id;
    private String name;
    private String country;
    private String base;
} 