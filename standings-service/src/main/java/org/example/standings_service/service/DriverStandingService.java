package org.example.standings_service.service;

import lombok.extern.slf4j.Slf4j;
import org.example.standings_service.client.ParticipantClient;
import org.example.standings_service.client.RaceResultClient;
import org.example.standings_service.model.Driver;
import org.example.standings_service.model.DriverRaceResult;
import org.example.standings_service.model.DriverStanding;
import org.example.standings_service.model.RaceStage;
import org.example.standings_service.model.RacingTeam;
import org.example.standings_service.model.Season;
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

    // Phương thức lấy xếp hạng tay đua theo đối tượng mùa giải (sử dụng tham chiếu đối tượng)
    public List<DriverStanding> getDriverStandingsBySeason(Season season) {
        return driverStandingRepository.findBySeasonOrderByTotalPointsDescRankAsc(season);
    }
    // Phương thức lấy xếp hạng tay đua theo đối tượng mùa giải và đối tượng tay đua (sử dụng tham chiếu đối tượng)
    public Optional<DriverStanding> getDriverStandingBySeasonAndDriver(Season season, Driver driver) {
        return driverStandingRepository.findBySeasonAndDriver(season, driver);
    }

    // Tính toán lại xếp hạng cho tay đua trong mùa giải (sử dụng đối tượng)
    @Transactional
    public void recalculateStandingForDriver(Season season, Driver driver) {
        // Kiểm tra nếu driver hoặc season null thì không xử lý
        if (driver == null || season == null) {
            log.warn("Không thể tính toán lại xếp hạng cho tay đua null hoặc mùa giải null");
            return;
        }
        
        log.info("Tính toán lại xếp hạng cho tay đua {} trong mùa giải {}", driver.getId(), season.getId());
        String driverId = driver.getId();
        String seasonId = season.getId();
        try {
            // Lấy tất cả kết quả đua xe cho tay đua trong mùa giải
            List<DriverRaceResult> results = raceResultClient.getResultsByDriverIdAndSeasonId(driverId, seasonId);
            log.info("Kết quả đua xe cho tay đua {} trong mùa giải {}", driverId, seasonId);
            if (results.isEmpty()) {
                log.info("Không tìm thấy kết quả đua xe cho tay đua {} trong mùa giải {}", driverId, seasonId);
                return;
            }

            // Nếu đã tồn tại, cập nhật điểm, wins và podiums
            Optional<DriverStanding> existingStanding = driverStandingRepository.findBySeasonAndDriver(season, driver);

            if (existingStanding.isPresent()) {
                DriverStanding standing = existingStanding.get();
                
                // Sử dụng strategy để tính toán, giữ nguyên thông tin tên và đội
                DriverStanding calculatedStanding = standingsCalculationStrategy.calculateDriverStanding(
                        seasonId,
                        driverId,
                        standing.getDriver() != null ? standing.getDriver().getFullName() : driverId,
                        standing.getTeam() != null ? standing.getTeam().getId() : null,
                        standing.getTeam() != null ? standing.getTeam().getName() : "Unknown Team",
                        standing.getDriver() != null ? standing.getDriver().getNationality() : "Unknown",
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
                    Driver newDriver = participantClient.getDriverById(driverId);
                    driverName = newDriver.getFullName();
                    nationality = newDriver.getNationality(); // Lấy quốc tịch từ driver
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

    // Tính toán lại xếp hạng cho tất cả tay đua trong mùa giải (sử dụng đối tượng)
    @Transactional
    public void recalculateStandingsForSeason(Season season) {
        if (season == null) {
            log.warn("Không thể tính toán lại xếp hạng cho mùa giải null");
            return;
        }
        
        log.info("Tính toán lại xếp hạng cho mùa giải {}", season.getId());
        try {
            Set<Driver> drivers = getDriversForSeason(season);
            for (Driver driver : drivers) {
                recalculateStandingForDriver(season, driver);
            }
            updateRanks(season);
        }
        catch (Exception e) {
            log.error("Lỗi tính toán lại xếp hạng cho mùa giải {}: {}", season.getId(), e.getMessage());
        }
    }

    // Tính toán lại xếp hạng sau khi cập nhật kết quả đua
    @Transactional
    public void recalculateStandingsAfterRaceUpdate(RaceStage raceStage, Season season) {
        if (raceStage == null || season == null) {
            log.warn("Không thể tính toán lại xếp hạng sau khi cập nhật kết quả đua cho null raceStage hoặc season. Race Stage ID: {}, Season ID: {}", 
                    raceStage != null ? raceStage.getId() : null, 
                    season != null ? season.getId() : null);
            return;
        }
        String raceStageId = raceStage.getId();
        String seasonId = season.getId();
        log.info("Tính toán lại xếp hạng sau khi cập nhật kết quả đua cho stage {} trong mùa giải {}", raceStageId, seasonId);
        try {
            // Lấy tất cả kết quả đua xe cho stage đua vừa cập nhật
            List<DriverRaceResult> raceResults = raceResultClient.getResultsByRaceStageId(raceStageId);
            
            // Xử lý từng kết quả để cập nhật bảng xếp hạng
            for (DriverRaceResult result : raceResults) {
                DriverRaceResult.DriverInfo driverInfo = result.getDriver();
                // Bỏ qua nếu driverId là null
                if (driverInfo == null) {
                    log.warn("Tìm thấy kết quả đua với driver null cho stage đua {}. Bỏ qua kết quả này.", raceStageId);
                    continue;
                }
                
                // Tạo đối tượng Driver từ DriverInfo
                Driver driver = new Driver();
                driver.setId(driverInfo.getId());
                driver.setFullName(driverInfo.getFullName());
                driver.setNationality(driverInfo.getNationality());
                
                // Tính toán lại toàn bộ điểm từ đầu cho tay đua này thay vì cập nhật gia tăng
                log.info("Tính toán lại toàn bộ điểm từ đầu");
                recalculateStandingForDriver(season, driver);
            }
            
            // Cập nhật ranks cho tất cả tay đua
            updateRanks(season);

        } catch (Exception e) {
            log.error("Lỗi tính toán lại xếp hạng sau khi cập nhật kết quả đua cho stage {} trong mùa giải {}: {}",
                    raceStageId, seasonId, e.getMessage());
        }
    }
    
    // Cập nhật rank cho tất cả tay đua trong mùa giải
    private void updateRanks(Season season) {
        if (season == null) {
            log.warn("Cannot update ranks for null season");
            return;
        }
        
        String seasonId = season.getId();
        List<DriverStanding> standings = driverStandingRepository.findBySeasonOrderByTotalPointsDescRankAsc(season);

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

    // Lấy tất cả tay đua trong mùa giải
    private Set<Driver> getDriversForSeason(Season season) {
        if (season == null) {
            log.warn("Cannot get drivers for null season");
            return Collections.emptySet();
        }
        
        List<DriverStanding> standings = driverStandingRepository.findBySeasonOrderByRankAsc(season);

        Set<Driver> drivers = new HashSet<>();
        for (DriverStanding standing : standings) {
            if (standing.getDriver() != null) {
                drivers.add(standing.getDriver());
            }
        }
        return drivers;
    }

    // Cập nhật thông tin tay đua trong xếp hạng khi thông tin tay đua được cập nhật
    @Transactional
    public void updateDriverInfo(String driverId, String fullName, String nationality) {
        if (driverId == null) {
            log.warn("Cannot update driver info for null driverId");
            return;
        }
        
        List<DriverStanding> standings = driverStandingRepository.findAll().stream()
                .filter(s -> s.getDriver() != null && driverId.equals(s.getDriver().getId()))
                .collect(Collectors.toList());

        for (DriverStanding standing : standings) {
            // Update the driver object instead
            Driver driver = standing.getDriver();
            if (driver != null) {
                driver.setFullName(fullName);
                driver.setNationality(nationality);
            }
            driverStandingRepository.save(standing);
        }
    }

    // Cập nhật thông tin đội trong xếp hạng khi thông tin đội được cập nhật
    @Transactional
    public void updateTeamInfo(String teamId, String name) {
        if (teamId == null) {
            log.warn("Cannot update team info for null teamId");
            return;
        }
        
        List<DriverStanding> standings = driverStandingRepository.findAll().stream()
                .filter(s -> s.getTeam() != null && teamId.equals(s.getTeam().getId()))
                .collect(Collectors.toList());

        for (DriverStanding standing : standings) {
            // Update the team object instead
            RacingTeam team = standing.getTeam();
            if (team != null) {
                team.setName(name);
            }
            driverStandingRepository.save(standing);
        }
    }
}