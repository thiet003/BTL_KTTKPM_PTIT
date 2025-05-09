package org.example.standings_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "driver_standings", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"season_id", "driver_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverStanding {
    @Id
    private String id;

    @Column(name = "season_id", nullable = false)
    private String seasonId;

    @Column(name = "driver_id", nullable = false)
    private String driverId;

    @Column(name = "team_id")
    private String teamId;

    @Column(name = "driver_name")
    private String driverName;

    @Column(name = "team_name")
    private String teamName;

    @Column(name = "nationality")
    private String nationality;

    @Column(name = "total_points")
    private Integer totalPoints;

    @Column(name = "`rank`")
    private Integer rank;

    private Integer wins;

    private Integer podiums;

    @Column(name = "last_calculated")
    private LocalDateTime lastCalculated;
}