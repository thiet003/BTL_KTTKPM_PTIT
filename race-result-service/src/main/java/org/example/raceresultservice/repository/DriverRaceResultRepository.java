package org.example.raceresultservice.repository;

import org.example.raceresultservice.model.DriverRaceResult;
import org.example.raceresultservice.model.Driver;
import org.example.raceresultservice.model.RaceStage;
import org.example.raceresultservice.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRaceResultRepository extends JpaRepository<DriverRaceResult, String> {
    List<DriverRaceResult> findByRaceStage(RaceStage raceStage);
    
    List<DriverRaceResult> findByRaceStageId(String raceStageId);

    List<DriverRaceResult> findByDriver(Driver driver);
    
    List<DriverRaceResult> findByDriverId(String driverId);
    
    Optional<DriverRaceResult> findByRaceStageAndDriver(RaceStage raceStage, Driver driver);
    
    Optional<DriverRaceResult> findByRaceStageIdAndDriverId(String raceStageId, String driverId);
    
    List<DriverRaceResult> findByDriverAndSeason(Driver driver, Season season);
    
    List<DriverRaceResult> findByDriverIdAndSeasonId(String driverId, String seasonId);
}