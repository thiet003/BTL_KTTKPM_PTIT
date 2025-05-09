package org.example.participantservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "racing_teams")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RacingTeam {
    @Id
    private String id;

    private String name;

    @Column(name = "base_location")
    private String baseLocation;

    private String manufacturer;

    private String country;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "team-assignment")
    private List<DriverTeamAssignment> assignments;
}