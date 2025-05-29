package org.example.raceresultservice.model;

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
@Table(name = "driver_refs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Driver {
    @Id
    private String id;
    
    @Column(name = "full_name")
    private String fullName;
    
    @Column(name = "nationality")
    private String nationality;
    
    @OneToMany(mappedBy = "driver")
    @JsonIgnoreProperties({"driver", "hibernateLazyInitializer", "handler"})
    private List<DriverTeamAssignment> teamAssignments;
} 