package org.example.raceresultservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "race_entries", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"race_stage_id", "driver_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceEntry {
    @Id
    private String id;

    @Column(name = "race_stage_id", nullable = false)
    private String raceStageId;

    @Column(name = "driver_id", nullable = false)
    private String driverId;

    @Column(name = "team_id", nullable = false)
    private String teamId;

    @Column(name = "car_number")
    private Integer carNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "entry_status")
    private EntryStatus entryStatus;

    @Column(name = "registered_at")
    private LocalDateTime registeredAt;
}