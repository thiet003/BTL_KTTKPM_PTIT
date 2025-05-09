package org.example.standings_service.strategy;


import org.example.standings_service.model.DriverRaceResult;
import org.example.standings_service.model.DriverStanding;
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
        DriverStanding standing = new DriverStanding();
        standing.setId(UUID.randomUUID().toString());
        standing.setSeasonId(seasonId);
        standing.setDriverId(driverId);
        standing.setDriverName(driverName);
        standing.setTeamId(teamId);
        standing.setTeamName(teamName);
        standing.setNationality(nationality);

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