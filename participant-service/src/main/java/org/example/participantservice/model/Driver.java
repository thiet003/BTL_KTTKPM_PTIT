package org.example.participantservice.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

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

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    @JsonManagedReference(value = "driver-assignment")
    private List<DriverTeamAssignment> assignments;
}