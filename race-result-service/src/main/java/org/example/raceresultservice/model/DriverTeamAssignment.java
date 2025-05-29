package org.example.raceresultservice.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Table(name = "driver_team_assignments_ref")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverTeamAssignment {
    @Id
    private String id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id", nullable = false)
    @JsonIgnoreProperties({"teamAssignments", "hibernateLazyInitializer", "handler"})
    private Driver driver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "team_id", nullable = false)
    @JsonIgnoreProperties({"driverAssignments", "hibernateLazyInitializer", "handler"})
    private Team team;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;
} 