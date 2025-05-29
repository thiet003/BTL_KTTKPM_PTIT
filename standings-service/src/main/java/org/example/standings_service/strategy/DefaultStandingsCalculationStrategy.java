package org.example.standings_service.strategy;

import org.example.standings_service.model.Driver;
import org.example.standings_service.model.DriverStanding;
import org.example.standings_service.model.RacingTeam;
import org.example.standings_service.model.Season;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Component
public class DefaultStandingsCalculationStrategy implements StandingsCalculationStrategy {

    @Override
    public DriverStanding calculateDriverStanding(String seasonId, String driverId, 
                                                 String teamId, List<Integer> racePoints) {
        // Tạo mới một đối tượng DriverStanding
        DriverStanding standing = new DriverStanding();
        standing.setId(UUID.randomUUID().toString());
        
        // Thiết lập tham chiếu đến đối tượng Season
        Season season = new Season();
        season.setId(seasonId);
        standing.setSeason(season);
        
        Driver driver = new Driver();
        driver.setId(driverId);
        standing.setDriver(driver);
        
        if (teamId != null) {
            RacingTeam team = new RacingTeam();
            team.setId(teamId);
            standing.setLastTeam(team);
        }

        // Tính toán điểm, thắng và podium
        int totalPoints = 0;
        int wins = 0;
        int podiums = 0;

        for (Integer points : racePoints) {
            if (points != null) {
                totalPoints += points;
                
                // Assuming standard F1 scoring: 25 points for 1st, 18 for 2nd, 15 for 3rd
                if (points == 25) {
                    wins++;
                    podiums++;
                } else if (points == 18 || points == 15) {
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