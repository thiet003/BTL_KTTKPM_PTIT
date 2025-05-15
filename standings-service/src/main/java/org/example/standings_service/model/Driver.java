package org.example.standings_service.model;

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
public class Driver {
    @Id
    private String id;
    
    @Column(name = "full_name")
    private String fullName;
    
    private String nationality;
    
    @OneToMany(mappedBy = "driver")
    private List<DriverStanding> driverStandings;
}