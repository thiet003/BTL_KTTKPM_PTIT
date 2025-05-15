package org.example.raceresultservice.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * Entity lưu trữ thông tin về chặng đua từ race-schedule-service
 */
@Entity
@Table(name = "race_stages_refs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceStage {
    @Id
    private String id;
    
    @Column(name = "name")
    private String name;
    
    @Column(name = "race_date")
    private LocalDate raceDate;
    
    @Column(name = "laps")
    private Integer laps;
    
    @Column(name = "circuit_name")
    private String circuitName;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "season_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Season season;
    
    @OneToMany(mappedBy = "raceStage")
    @JsonIgnoreProperties({"raceStage", "hibernateLazyInitializer", "handler"})
    private List<DriverRaceResult> raceResults;
} 