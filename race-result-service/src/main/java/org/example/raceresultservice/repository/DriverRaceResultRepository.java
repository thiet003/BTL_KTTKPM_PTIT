package org.example.raceresultservice.repository;

import org.example.raceresultservice.model.Driver;
import org.example.raceresultservice.model.DriverRaceResult;
import org.example.raceresultservice.model.DriverTeamAssignment;
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

    List<DriverRaceResult> findByDriverTeamAssignment(DriverTeamAssignment driverTeamAssignment);
    
    List<DriverRaceResult> findByDriverTeamAssignmentId(String driverTeamAssignmentId);
    
    Optional<DriverRaceResult> findByRaceStageAndDriverTeamAssignment(RaceStage raceStage, DriverTeamAssignment driverTeamAssignment);
    
    Optional<DriverRaceResult> findByRaceStageIdAndDriverTeamAssignmentId(String raceStageId, String driverTeamAssignmentId);
    
    List<DriverRaceResult> findByDriverTeamAssignmentAndSeason(DriverTeamAssignment driverTeamAssignment, Season season);
    
    @Query("SELECT drr FROM DriverRaceResult drr WHERE drr.driverTeamAssignment.driver.id = :driverId")
    List<DriverRaceResult> findByDriverId(@Param("driverId") String driverId);
    
    @Query("SELECT drr FROM DriverRaceResult drr WHERE drr.driverTeamAssignment.driver.id = :driverId AND drr.season.id = :seasonId")
    List<DriverRaceResult> findByDriverIdAndSeasonId(@Param("driverId") String driverId, @Param("seasonId") String seasonId);

    // Lấy kết quả đua theo tay đua và mùa giải(theo hướng đối tượng)
    @Query("SELECT drr FROM DriverRaceResult drr WHERE drr.driverTeamAssignment.driver = :driver AND drr.season = :season")
    List<DriverRaceResult> findByDriverAndSeason(Driver driver, Season season);

    @Query("SELECT drr FROM DriverRaceResult drr WHERE drr.raceStage.id = :raceStageId AND drr.driverTeamAssignment.driver.id = :driverId")
    Optional<DriverRaceResult> findByRaceStageIdAndDriverId(@Param("raceStageId") String raceStageId, @Param("driverId") String driverId);
}