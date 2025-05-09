package org.example.racescheduleservice.repository;

import org.example.racescheduleservice.model.Circuit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CircuitRepository extends JpaRepository<Circuit, String> {
}