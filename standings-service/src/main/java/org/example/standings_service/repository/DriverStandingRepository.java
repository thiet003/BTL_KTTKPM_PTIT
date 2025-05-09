package org.example.standings_service.repository;

import org.example.standings_service.model.DriverStanding;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverStandingRepository extends JpaRepository<DriverStanding, String> {
    // Lấy tất cả xếp hạng tay đua theo mùa giải
    List<DriverStanding> findBySeasonIdOrderByRankAsc(String seasonId);
    // Lấy tất cả xếp hạng tay đua theo mùa giải và điểm
    List<DriverStanding> findBySeasonIdOrderByTotalPointsDescRankAsc(String seasonId);
    // Lấy xếp hạng tay đua theo mùa giải và ID tay đua
    Optional<DriverStanding> findBySeasonIdAndDriverId(String seasonId, String driverId);
}