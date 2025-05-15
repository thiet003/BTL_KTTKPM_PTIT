package org.example.standings_service.repository;

import org.example.standings_service.model.Driver;
import org.example.standings_service.model.DriverStanding;
import org.example.standings_service.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DriverStandingRepository extends JpaRepository<DriverStanding, String> {
    // Lấy tất cả xếp hạng tay đua theo mùa giải
    List<DriverStanding> findBySeasonOrderByRankAsc(Season season);

    // Lấy tất cả xếp hạng tay đua theo mùa giải và điểm
    List<DriverStanding> findBySeasonOrderByTotalPointsDescRankAsc(Season season);

    // Lấy xếp hạng tay đua theo mùa giải và tay đua
    Optional<DriverStanding> findBySeasonAndDriver(Season season, Driver driver);

}