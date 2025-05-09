package org.example.raceresultservice.model;

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

    @Column(name = "race_stage_id", nullable = false)
    private String raceStageId;

    @Column(name = "driver_id", nullable = false)
    private String driverId;

    @Column(name = "team_id", nullable = false)
    private String teamId;

    @Column(name = "driver_name", nullable = true)
    private String driverName;

    @Column(name = "team_name", nullable = true)
    private String teamName;

    @Column(name = "season_id", nullable = false)
    private String seasonId;

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