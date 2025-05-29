package org.example.standings_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity lưu trữ thông tin về đội đua
 */
@Entity
@Table(name = "teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class RacingTeam {
    @Id
    private String id;
    
    private String name;
    
    private String country;
    
    @OneToMany(mappedBy = "lastTeam", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"lastTeam", "driver", "season", "hibernateLazyInitializer", "handler"})
    private List<DriverStanding> driverStandings;
    
    @OneToMany(mappedBy = "team", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"team", "driver", "hibernateLazyInitializer", "handler"})
    private List<DriverTeamAssignment> driverAssignments;
}