package org.example.raceresultservice.repository;

import org.example.raceresultservice.model.RaceEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceEntryRepository extends JpaRepository<RaceEntry, String> {
    List<RaceEntry> findByRaceStageId(String raceStageId);
}