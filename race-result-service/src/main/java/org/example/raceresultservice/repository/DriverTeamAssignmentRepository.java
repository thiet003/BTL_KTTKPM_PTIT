package org.example.raceresultservice.repository;

import org.example.raceresultservice.model.DriverTeamAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverTeamAssignmentRepository extends JpaRepository<DriverTeamAssignment, String> {
    
    List<DriverTeamAssignment> findByDriverId(String driverId);
    
    List<DriverTeamAssignment> findByTeamId(String teamId);
    
    @Query("SELECT dta FROM DriverTeamAssignment dta WHERE dta.driver.id = :driverId AND dta.team.id = :teamId")
    List<DriverTeamAssignment> findByDriverIdAndTeamId(@Param("driverId") String driverId, @Param("teamId") String teamId);
    
    @Query("SELECT dta FROM DriverTeamAssignment dta WHERE dta.driver.id = :driverId " +
           "AND (:date IS NULL OR dta.startDate <= :date) " +
           "AND (:date IS NULL OR dta.endDate IS NULL OR dta.endDate >= :date)")
    List<DriverTeamAssignment> findByDriverIdAndDate(@Param("driverId") String driverId, @Param("date") LocalDate date);
    
    @Query("SELECT dta FROM DriverTeamAssignment dta WHERE dta.driver.id = :driverId AND dta.team.id = :teamId " +
           "AND (:date IS NULL OR dta.startDate <= :date) " +
           "AND (:date IS NULL OR dta.endDate IS NULL OR dta.endDate >= :date)")
    Optional<DriverTeamAssignment> findByDriverIdAndTeamIdAndDate(@Param("driverId") String driverId, 
                                                                 @Param("teamId") String teamId, 
                                                                 @Param("date") LocalDate date);
} 