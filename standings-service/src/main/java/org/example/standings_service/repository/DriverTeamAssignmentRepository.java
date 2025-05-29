package org.example.standings_service.repository;

import org.example.standings_service.model.Driver;
import org.example.standings_service.model.DriverTeamAssignment;
import org.example.standings_service.model.RacingTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DriverTeamAssignmentRepository extends JpaRepository<DriverTeamAssignment, String> {
    
    List<DriverTeamAssignment> findByDriver(Driver driver);
    
    List<DriverTeamAssignment> findByTeam(RacingTeam team);
    
    List<DriverTeamAssignment> findByDriverId(String driverId);
    
    List<DriverTeamAssignment> findByTeamId(String teamId);
    
    // Find assignments active at a specific date
    @Query("SELECT dta FROM DriverTeamAssignment dta WHERE dta.driver.id = :driverId " +
           "AND dta.startDate <= :date AND (dta.endDate IS NULL OR dta.endDate >= :date)")
    List<DriverTeamAssignment> findByDriverIdAndActiveDate(@Param("driverId") String driverId, 
                                                           @Param("date") LocalDate date);
    
    // Find the most recent assignment for a driver
    @Query("SELECT dta FROM DriverTeamAssignment dta WHERE dta.driver.id = :driverId " +
           "ORDER BY dta.endDate DESC, dta.startDate DESC")
    List<DriverTeamAssignment> findByDriverIdOrderByEndDateDesc(@Param("driverId") String driverId);
    
    // Find current assignment for a driver (where end date is null or in the future)
    @Query("SELECT dta FROM DriverTeamAssignment dta WHERE dta.driver.id = :driverId " +
           "AND (dta.endDate IS NULL OR dta.endDate >= CURRENT_DATE) " +
           "ORDER BY dta.startDate DESC")
    Optional<DriverTeamAssignment> findCurrentAssignmentByDriverId(@Param("driverId") String driverId);
    
    // Find assignments within a date range
    @Query("SELECT dta FROM DriverTeamAssignment dta WHERE " +
           "dta.startDate <= :endDate AND (dta.endDate IS NULL OR dta.endDate >= :startDate)")
    List<DriverTeamAssignment> findAssignmentsInDateRange(@Param("startDate") LocalDate startDate, 
                                                          @Param("endDate") LocalDate endDate);
} 