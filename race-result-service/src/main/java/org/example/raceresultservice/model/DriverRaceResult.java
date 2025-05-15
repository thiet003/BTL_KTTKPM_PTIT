package org.example.raceresultservice.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "driver_race_results", uniqueConstraints = {
        @UniqueConstraint(name = "unique_race_driver_result", columnNames = {"race_stage_id", "driver_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverRaceResult {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "race_stage_id", nullable = false)
    @JsonIgnoreProperties({"raceResults", "hibernateLazyInitializer", "handler"})
    private RaceStage raceStage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id", nullable = false)
    @JsonIgnoreProperties({"raceResults", "hibernateLazyInitializer", "handler"})
    private Driver driver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = false)
    @JsonIgnoreProperties({"raceResults", "hibernateLazyInitializer", "handler"})
    private Team team;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "season_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Season season;

    @Column(name = "grid_position", nullable = true)
    private Integer gridPosition;

    @Column(name = "finish_position", nullable = true)
    private Integer finishPosition;

    @Column(nullable = true)
    private Integer points;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "VARCHAR(20) DEFAULT 'FINISHED'")
    private RaceStatus status;

    @Column(name = "finish_time_or_gap", nullable = true)
    private String finishTimeOrGap;

    @Column(name = "laps_completed", nullable = true)
    private Integer lapsCompleted;
}