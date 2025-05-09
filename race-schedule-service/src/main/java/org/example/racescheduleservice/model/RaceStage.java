package org.example.racescheduleservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "race_stages")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceStage {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "season_id", nullable = false)
    @JsonBackReference
    private Season season;

    @ManyToOne
    @JoinColumn(name = "circuit_id", nullable = false)
    @JsonBackReference(value = "circuit-racestage")
    private Circuit circuit;
    
    @JsonProperty("circuitId")
    public String getCircuitId() {
        return circuit != null ? circuit.getId() : null;
    }

    private String name;

    @Column(name = "race_date")
    private LocalDate raceDate;

    private Integer laps;

    @Enumerated(EnumType.STRING)
    private RaceStatus status;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
