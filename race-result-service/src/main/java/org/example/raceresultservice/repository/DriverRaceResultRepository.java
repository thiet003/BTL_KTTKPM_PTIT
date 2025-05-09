package org.example.raceresultservice.repository;

import org.example.raceresultservice.model.DriverRaceResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverRaceResultRepository extends JpaRepository<DriverRaceResult, String> {
    List<DriverRaceResult> findByRaceStageId(String raceStageId);

    List<DriverRaceResult> findByDriverId(String driverId);
    
    Optional<DriverRaceResult> findByRaceStageIdAndDriverId(String raceStageId, String driverId);
    
    List<DriverRaceResult> findByDriverIdAndSeasonId(String driverId, String seasonId);
}