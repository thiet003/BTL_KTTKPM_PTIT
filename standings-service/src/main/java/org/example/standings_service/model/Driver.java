package org.example.standings_service.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity lưu trữ thông tin về tay đua
 */
@Entity
@Table(name = "drivers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Driver {
    @Id
    private String id;
    
    @Column(name = "full_name")
    private String fullName;
    
    private String nationality;
    
    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"driver", "season", "lastTeam", "hibernateLazyInitializer", "handler"})
    private List<DriverStanding> driverStandings;
    
    @OneToMany(mappedBy = "driver", fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"driver", "team", "hibernateLazyInitializer", "handler"})
    private List<DriverTeamAssignment> teamAssignments;
}