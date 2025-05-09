package org.example.racescheduleservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "circuits")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Circuit {
    @Id
    private String id;

    private String name;

    private String location;

    private String country;

    @Column(name = "length_km")
    private Double lengthKm;

    @OneToMany(mappedBy = "circuit", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "circuit-racestage")
    private List<RaceStage> raceStages;
}