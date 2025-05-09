package org.example.raceresultservice.model;

import lombok.Data;

/**
 * Model class for consuming driver data from participant-service
 */
@Data
public class Driver {
    private String id;
    private String fullName;
    private String nationality;
} 