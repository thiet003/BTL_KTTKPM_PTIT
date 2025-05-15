package org.example.standings_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "season_id", nullable = false)
    @JsonIgnoreProperties("driverStandings")
    private Season season;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id", nullable = false)
    @JsonIgnoreProperties("driverStandings")
    private Driver driver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id")
    @JsonIgnoreProperties("driverStandings")
    private RacingTeam team;

    @Column(name = "total_points")
    private Integer totalPoints;

    @Column(name = "`rank`")
    private Integer rank;

    private Integer wins;

    private Integer podiums;

    @Column(name = "last_calculated")
    private LocalDateTime lastCalculated;
}