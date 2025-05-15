package org.example.standings_service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Entity lưu trữ thông tin về mùa giải
 */
@Entity
@Table(name = "seasons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Season {
    @Id
    private String id;
    
    private String name;
    
    private Integer year;
    
    @OneToMany(mappedBy = "season")
    private List<DriverStanding> driverStandings;
} 