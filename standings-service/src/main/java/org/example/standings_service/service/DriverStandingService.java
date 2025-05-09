package org.example.standings_service.service;

import lombok.extern.slf4j.Slf4j;
import org.example.standings_service.client.ParticipantClient;
import org.example.standings_service.client.RaceResultClient;
import org.example.standings_service.model.Driver;
import org.example.standings_service.model.DriverRaceResult;
import org.example.standings_service.model.DriverStanding;
import org.example.standings_service.model.RacingTeam;
import org.example.standings_service.repository.DriverStandingRepository;
import org.example.standings_service.strategy.StandingsCalculationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DriverStandingService {

    @Autowired
    private DriverStandingRepository driverStandingRepository;

    @Autowired
    private RaceResultClient raceResultClient;

    @Autowired
    private ParticipantClient participantClient;

    @Autowired
    private StandingsCalculationStrategy standingsCalculationStrategy;

    public List<DriverStanding> getAllDriverStandings() {
        return driverStandingRepository.findAll();
    }

    public List<DriverStanding> getDriverStandingsBySeasonId(String seasonId) {
        return driverStandingRepository.findBySeasonIdOrderByTotalPointsDescRankAsc(seasonId);
    }

    public Optional<DriverStanding> getDriverStandingBySeasonIdAndDriverId(String seasonId, String driverId) {
        return driverStandingRepository.findBySeasonIdAndDriverId(seasonId, driverId);
    }

    // Tính toán lại xếp hạng cho tay đua trong mùa giải
    @Transactional
    public void recalculateStandingForDriver(String seasonId, String driverId) {
        log.info("Recalculating standings for driver {} in season {}", driverId, seasonId);

        try {
            // Lấy tất cả kết quả đua xe cho tay đua trong mùa giải
            List<DriverRaceResult> results = raceResultClient.getResultsByDriverIdAndSeasonId(driverId, seasonId);
            log.info("Results: {}", results);
            if (results.isEmpty()) {
                log.info("No results found for driver {} in season {}", driverId, seasonId);
                return;
            }

            // Nếu đã tồn tại, cập nhật điểm, wins và podiums
            Optional<DriverStanding> existingStanding = driverStandingRepository.findBySeasonIdAndDriverId(seasonId, driverId);

            if (existingStanding.isPresent()) {
                DriverStanding standing = existingStanding.get();
                
                // Sử dụng strategy để tính toán, giữ nguyên thông tin tên và đội
                DriverStanding calculatedStanding = standingsCalculationStrategy.calculateDriverStanding(
                        seasonId,
                        driverId,
                        standing.getDriverName(), // Giữ nguyên tên
                        standing.getTeamId(),     // Giữ nguyên team ID
                        standing.getTeamName(),   // Giữ nguyên team name
                        standing.getNationality(), // Giữ nguyên quốc tịch
                        results
                );
                
                // Chỉ cập nhật thông tin điểm số
                standing.setTotalPoints(calculatedStanding.getTotalPoints());
                standing.setWins(calculatedStanding.getWins());
                standing.setPodiums(calculatedStanding.getPodiums());
                standing.setLastCalculated(calculatedStanding.getLastCalculated());
                driverStandingRepository.save(standing);
            } else {
                // Tạo mới bảng xếp hạng
                String driverName = driverId; // Default là ID driver
                String teamId = null;
                String teamName = "Unknown Team";
                String nationality = "Unknown"; // Default quốc tịch
                
                try {
                    // Lấy thông tin driver nếu có thể
                    Driver driver = participantClient.getDriverById(driverId);
                    driverName = driver.getFullName();
                    nationality = driver.getNationality(); // Lấy quốc tịch từ driver
                } catch (Exception e) {
                    log.warn("Could not fetch driver details for {}, using driver ID as name: {}", driverId, e.getMessage());
                }
                // Lấy team từ kết quả đua gần nhất
                teamId = results.stream()
                        .sorted(Comparator.comparing(DriverRaceResult::getRaceStageId).reversed())
                        .findFirst()
                        .map(DriverRaceResult::getTeamId)
                        .orElse(null);

                if (teamId == null) {
                    log.warn("Could not determine team for driver {}", driverId);
                    return;
                }

                try {
                    // Lấy thông tin team nếu có thể
                    RacingTeam team = participantClient.getTeamById(teamId);
                    if (team != null && team.getName() != null) {
                        teamName = team.getName();
                    }
                } catch (Exception e) {
                    log.warn("Could not fetch team details for {}, using default team name: {}", teamId, e.getMessage());
                }

                // Tạo mới bảng xếp hạng sử dụng Strategy
                DriverStanding newStanding = standingsCalculationStrategy.calculateDriverStanding(
                        seasonId,
                        driverId,
                        driverName,
                        teamId,
                        teamName,
                        nationality,
                        results
                );
                
                if (newStanding == null) {
                    log.error("Strategy returned null standing for driver {} in season {}", driverId, seasonId);
                    return;
                }
                
                driverStandingRepository.save(newStanding);
            }

        } catch (Exception e) {
            log.error("Error recalculating standings for driver {} in season {}: {}", driverId, seasonId, e.getMessage());
        }
    }

    // Tính toán lại xếp hạng cho tất cả tay đua trong mùa giải
    @Transactional
    public void recalculateStandingsForSeason(String seasonId) {
        log.info("Recalculating standings for season {}", seasonId);

        try {
            // Get all driver IDs with results in this season
            Set<String> driverIds = getDriverIdsForSeason(seasonId);

            // Recalculate standings for each driver
            for (String driverId : driverIds) {
                recalculateStandingForDriver(seasonId, driverId);
            }
            // Cập nhật ranks cho tất cả tay đua
            updateRanks(seasonId);

        } catch (Exception e) {
            log.error("Error recalculating standings for season {}: {}", seasonId, e.getMessage());
        }
    }

    // Tính toán lại xếp hạng sau khi cập nhật kết quả đua
    @Transactional
    public void recalculateStandingsAfterRaceUpdate(String raceStageId, String seasonId) {
        log.info("Recalculating standings after race update for stage {} in season {}", raceStageId, seasonId);

        try {
            // Lấy tất cả kết quả đua xe cho stage đua vừa cập nhật
            List<DriverRaceResult> raceResults = raceResultClient.getResultsByRaceStageId(raceStageId);
            
            // Xử lý từng kết quả để cập nhật bảng xếp hạng
            for (DriverRaceResult result : raceResults) {
                String driverId = result.getDriverId();
                
                // Tính toán lại toàn bộ điểm từ đầu cho tay đua này thay vì cập nhật gia tăng
                log.info("Recalculating complete standings for driver {} in season {}", driverId, seasonId);
                recalculateStandingForDriver(seasonId, driverId);
            }
            
            // Cập nhật ranks cho tất cả tay đua
            updateRanks(seasonId);

        } catch (Exception e) {
            log.error("Error recalculating standings after race update for stage {} in season {}: {}",
                    raceStageId, seasonId, e.getMessage());
        }
    }

    // Cập nhật rank cho tất cả tay đua trong mùa giải
    private void updateRanks(String seasonId) {
        List<DriverStanding> standings = driverStandingRepository.findBySeasonIdOrderByTotalPointsDescRankAsc(seasonId);

        int rank = 1;
        Integer lastPoints = null;
        Integer lastRank = null;

        for (DriverStanding standing : standings) {
            if (lastPoints != null && standing.getTotalPoints().equals(lastPoints)) {
                standing.setRank(lastRank);  // Same rank for tied drivers
            } else {
                standing.setRank(rank);
                lastRank = rank;
                lastPoints = standing.getTotalPoints();
            }

            driverStandingRepository.save(standing);
            rank++;
        }
    }

    // Lấy tất cả id tay đua trong mùa giải
    private Set<String> getDriverIdsForSeason(String seasonId) {
        List<DriverStanding> standings = driverStandingRepository.findBySeasonIdOrderByRankAsc(seasonId);

        Set<String> driverIds = standings.stream()
                .map(DriverStanding::getDriverId)
                .collect(Collectors.toSet());

        if (driverIds.isEmpty()) {
            log.info("No existing standings found for season {}", seasonId);
        }

        return driverIds;
    }

    // Cập nhật thông tin tay đua trong xếp hạng khi thông tin tay đua được cập nhật
    @Transactional
    public void updateDriverInfo(String driverId, String fullName, String nationality) {
        List<DriverStanding> standings = driverStandingRepository.findAll().stream()
                .filter(s -> s.getDriverId().equals(driverId))
                .collect(Collectors.toList());

        for (DriverStanding standing : standings) {
            standing.setDriverName(fullName);
            standing.setNationality(nationality);
            driverStandingRepository.save(standing);
        }
    }

    // Cập nhật thông tin đội trong xếp hạng khi thông tin đội được cập nhật
    @Transactional
    public void updateTeamInfo(String teamId, String name) {
        List<DriverStanding> standings = driverStandingRepository.findAll().stream()
                .filter(s -> s.getTeamId().equals(teamId))
                .collect(Collectors.toList());

        for (DriverStanding standing : standings) {
            standing.setTeamName(name);
            driverStandingRepository.save(standing);
        }
    }
}