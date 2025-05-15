package org.example.standings_service.model;

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
public class RacingTeam {
    @Id
    private String id;
    
    private String name;
    
    private String country;
    
    @OneToMany(mappedBy = "team")
    private List<DriverStanding> driverStandings;
}