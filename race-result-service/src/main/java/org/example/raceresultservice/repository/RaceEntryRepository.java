package org.example.raceresultservice.repository;

import org.example.raceresultservice.model.DriverTeamAssignment;
import org.example.raceresultservice.model.RaceEntry;
import org.example.raceresultservice.model.RaceStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceEntryRepository extends JpaRepository<RaceEntry, String> {
    List<RaceEntry> findByRaceStageId(String raceStageId);
    List<RaceEntry> findByRaceStage(RaceStage raceStage);
}