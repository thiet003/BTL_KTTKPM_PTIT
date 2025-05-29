package org.example.standings_service.service;

import lombok.extern.slf4j.Slf4j;
import org.example.standings_service.client.ParticipantClient;
import org.example.standings_service.client.RaceResultClient;
import org.example.standings_service.model.*;
import org.example.standings_service.repository.DriverStandingRepository;
import org.example.standings_service.repository.DriverTeamAssignmentRepository;
import org.example.standings_service.strategy.StandingsCalculationStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DriverStandingService {

    @Autowired
    private DriverStandingRepository driverStandingRepository;

    @Autowired
    private DriverTeamAssignmentRepository driverTeamAssignmentRepository;

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

    // Tìm team hiện tại của driver (team có hợp đồng gần nhất hoặc hiện tại)
    private RacingTeam findCurrentTeamForDriver(String driverId) {
        Optional<DriverTeamAssignment> currentAssignment = driverTeamAssignmentRepository.findCurrentAssignmentByDriverId(driverId);
        if (currentAssignment.isPresent()) {
            return currentAssignment.get().getTeam();
        }
        
        // Nếu không có assignment hiện tại, lấy assignment gần nhất
        List<DriverTeamAssignment> assignments = driverTeamAssignmentRepository.findByDriverIdOrderByEndDateDesc(driverId);
        if (!assignments.isEmpty()) {
            return assignments.get(0).getTeam();
        }
        
        return null;
    }

    // Tính toán lại xếp hạng cho tay đua trong mùa giải (sử dụng đối tượng)
    @Transactional
    public void recalculateStandingForDriver(Season season, Driver driver) {
        if (season == null || driver == null) {
            log.warn("Cannot recalculate standing for null season or driver. Season: {}, Driver: {}", 
                    season != null ? season.getId() : null, 
                    driver != null ? driver.getId() : null);
            return;
        }
        
        String seasonId = season.getId();
        String driverId = driver.getId();
        
        log.info("Recalculating standing for driver {} in season {}", driverId, seasonId);
        
        try {
            // Lấy tất cả kết quả đua của tay đua trong mùa giải
            List<DriverRaceResult> results = raceResultClient.getResultsByDriverIdAndSeasonId(driverId, seasonId);
            
            // Tìm team hiện tại của driver
            RacingTeam currentTeam = findCurrentTeamForDriver(driverId);
            
            // Extract points from race results
            List<Integer> racePoints = results.stream()
                    .map(result -> result.getPoints() != null ? result.getPoints() : 0)
                    .collect(Collectors.toList());
            
            // Kiểm tra xem đã có bảng xếp hạng cho tay đua này chưa
            Optional<DriverStanding> existingStanding = driverStandingRepository.findBySeasonAndDriver(season, driver);
            
            if (existingStanding.isPresent()) {
                // Cập nhật bảng xếp hạng hiện có
                DriverStanding standing = existingStanding.get();
                standing.setLastTeam(currentTeam);
                
                // Sử dụng strategy để tính toán với interface mới
                DriverStanding calculatedStanding = standingsCalculationStrategy.calculateDriverStanding(
                        seasonId,
                        driverId,
                        currentTeam != null ? currentTeam.getId() : null,
                        racePoints
                );
                
                // Chỉ cập nhật thông tin điểm số
                standing.setTotalPoints(calculatedStanding.getTotalPoints());
                standing.setWins(calculatedStanding.getWins());
                standing.setPodiums(calculatedStanding.getPodiums());
                standing.setLastCalculated(calculatedStanding.getLastCalculated());
                driverStandingRepository.save(standing);
            } else {
                // Tạo mới bảng xếp hạng sử dụng Strategy với interface mới
                DriverStanding newStanding = standingsCalculationStrategy.calculateDriverStanding(
                        seasonId,
                        driverId,
                        currentTeam != null ? currentTeam.getId() : null,
                        racePoints
                );
                
                if (newStanding == null) {
                    log.error("Strategy returned null standing for driver {} in season {}", driverId, seasonId);
                    return;
                }
                
                // Set lastTeam cho standing mới
                newStanding.setLastTeam(currentTeam);
                
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
                // Lấy driver info từ DriverTeamAssignment
                DriverRaceResult.DriverInfo driverInfo = result.getDriver();
                // Bỏ qua nếu driverId là null
                if (driverInfo == null) {
                    log.warn("Tìm thấy kết quả đua với driver null cho stage đua {}. Bỏ qua kết quả này.", raceStageId);
                    continue;
                }
                
                // Tạo đối tượng Driver từ DriverInfo (từ DriverTeamAssignment)
                Driver driver = new Driver();
                driver.setId(driverInfo.getId());
                driver.setFullName(driverInfo.getFullName());
                driver.setNationality(driverInfo.getNationality());
                
                // Tính toán lại toàn bộ điểm từ đầu cho tay đua này thay vì cập nhật gia tăng
                log.info("Tính toán lại toàn bộ điểm từ đầu cho driver {} từ DriverTeamAssignment", driverInfo.getId());
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
                .filter(s -> s.getLastTeam() != null && teamId.equals(s.getLastTeam().getId()))
                .collect(Collectors.toList());

        for (DriverStanding standing : standings) {
            // Update the lastTeam object instead
            RacingTeam team = standing.getLastTeam();
            if (team != null) {
                team.setName(name);
            }
            driverStandingRepository.save(standing);
        }
    }
}