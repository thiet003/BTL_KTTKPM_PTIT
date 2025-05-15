package org.example.standings_service.strategy;


import org.example.standings_service.model.Driver;
import org.example.standings_service.model.DriverRaceResult;
import org.example.standings_service.model.DriverStanding;
import org.example.standings_service.model.RacingTeam;
import org.example.standings_service.model.Season;
import org.example.standings_service.repository.DriverStandingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class DefaultStandingsCalculationStrategy implements StandingsCalculationStrategy {

    @Override
    public DriverStanding calculateDriverStanding(String seasonId, String driverId, String driverName, 
                                                 String teamId, String teamName, String nationality,
                                                 List<DriverRaceResult> results) {
        // Tạo mới một đối tượng DriverStanding
        DriverStanding standing = new DriverStanding();
        standing.setId(UUID.randomUUID().toString());
        
        // Thiết lập tham chiếu đến đối tượng Season
        Season season = new Season();
        season.setId(seasonId);
        standing.setSeason(season);
        
        Driver driver = new Driver();
        driver.setId(driverId);
        driver.setFullName(driverName);
        driver.setNationality(nationality);
        standing.setDriver(driver);
        
        if (teamId != null) {
            RacingTeam team = new RacingTeam();
            team.setId(teamId);
            team.setName(teamName);
            standing.setTeam(team);
        }

        // Tính toán điểm, thắng và podium
        int totalPoints = 0;
        int wins = 0;
        int podiums = 0;

        for (DriverRaceResult result : results) {
            if (result.getPoints() != null) {
                totalPoints += result.getPoints();
            }

            if (result.getFinishPosition() != null) {
                if (result.getFinishPosition() == 1) {
                    wins++;
                }

                if (result.getFinishPosition() <= 3) {
                    podiums++;
                }
            }
        }

        standing.setTotalPoints(totalPoints);
        standing.setWins(wins);
        standing.setPodiums(podiums);
        standing.setLastCalculated(LocalDateTime.now());
        standing.setRank(0);
        return standing;
    }
    
    @Override
    public DriverStanding calculateDriverStanding(Season season, Driver driver, String driverName,
                                                 RacingTeam team, String teamName, String nationality,
                                                 List<DriverRaceResult> results) {
        // Tạo mới một đối tượng DriverStanding
        DriverStanding standing = new DriverStanding();
        standing.setId(UUID.randomUUID().toString());
        
        // Set entity references
        standing.setSeason(season);
        standing.setDriver(driver);
        standing.setTeam(team);
        
        // Đảm bảo các thuộc tính của tay đua được thiết lập
        if (driver != null) {
            if (driver.getFullName() == null) {
                driver.setFullName(driverName);
            }
            if (driver.getNationality() == null) {
                driver.setNationality(nationality);
            }
        }
        
        // Đảm bảo các thuộc tính của đội đua được thiết lập
        if (team != null && team.getName() == null) {
            team.setName(teamName);
        }

        // Tính toán điểm, thắng và podium
        int totalPoints = 0;
        int wins = 0;
        int podiums = 0;

        for (DriverRaceResult result : results) {
            if (result.getPoints() != null) {
                totalPoints += result.getPoints();
            }

            if (result.getFinishPosition() != null) {
                if (result.getFinishPosition() == 1) {
                    wins++;
                }

                if (result.getFinishPosition() <= 3) {
                    podiums++;
                }
            }
        }

        standing.setTotalPoints(totalPoints);
        standing.setWins(wins);
        standing.setPodiums(podiums);
        standing.setLastCalculated(LocalDateTime.now());
        standing.setRank(0);
        return standing;
    }
}