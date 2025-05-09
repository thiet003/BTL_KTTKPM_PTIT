package org.example.participantservice.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "driver_team_assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverTeamAssignment {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    @JsonBackReference(value = "driver-assignment")
    private Driver driver;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    @JsonBackReference(value = "team-assignment")
    private RacingTeam team;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
}