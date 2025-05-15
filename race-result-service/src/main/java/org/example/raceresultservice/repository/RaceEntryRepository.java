package org.example.raceresultservice.repository;

import org.example.raceresultservice.model.Driver;
import org.example.raceresultservice.model.RaceEntry;
import org.example.raceresultservice.model.RaceStage;
import org.example.raceresultservice.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceEntryRepository extends JpaRepository<RaceEntry, String> {
    List<RaceEntry> findByRaceStageId(String raceStageId);
    List<RaceEntry> findByRaceStage(RaceStage raceStage);
    List<RaceEntry> findByDriver(Driver driver);
    List<RaceEntry> findByTeam(Team team);
    List<RaceEntry> findByRaceStageAndDriver(RaceStage raceStage, Driver driver);
}