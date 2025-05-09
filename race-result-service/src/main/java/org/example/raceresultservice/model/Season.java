package org.example.raceresultservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Season entity for reference in queries
 */
@Entity
@Table(name = "seasons_ref")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Season {
    @Id
    private String id;

    private String name;
}