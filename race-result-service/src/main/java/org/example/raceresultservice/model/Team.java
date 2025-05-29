package org.example.raceresultservice.model;

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
@Table(name = "team_refs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id
    private String id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "country")
    private String country;
    
    @Column(name = "base")
    private String base;
    
    @OneToMany(mappedBy = "team")
    @JsonIgnoreProperties({"team", "hibernateLazyInitializer", "handler"})
    private List<DriverTeamAssignment> driverAssignments;
} 