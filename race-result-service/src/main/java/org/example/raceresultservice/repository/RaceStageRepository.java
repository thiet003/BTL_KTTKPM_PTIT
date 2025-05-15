package org.example.raceresultservice.repository;

import org.example.raceresultservice.model.RaceStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceStageRepository extends JpaRepository<RaceStage, String> {
    List<RaceStage> findBySeasonId(String seasonId);
} 