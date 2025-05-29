package org.example.raceresultservice.service;

import org.example.raceresultservice.event.RaceResultUpdatedEvent;
import org.example.raceresultservice.model.*;
import org.example.raceresultservice.repository.*;
import org.example.raceresultservice.strategy.PointsCalculationStrategy;
import org.example.raceresultservice.client.ParticipantClient;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class DriverRaceResultService {

    @Autowired
    private DriverRaceResultRepository driverRaceResultRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private PointsCalculationStrategy pointsCalculationStrategy;

    @Autowired
    private ParticipantClient participantClient;
    
    @Autowired
    private DriverRepository driverRepository;
    
    @Autowired
    private TeamRepository teamRepository;
    
    @Autowired
    private DriverTeamAssignmentRepository driverTeamAssignmentRepository;
    
    @Autowired
    private RaceStageRepository raceStageRepository;
    
    @Autowired
    private SeasonRepository seasonRepository;
    
    // Lấy tất cả kết quả đua
    public List<DriverRaceResult> getAllDriverRaceResults() {
        return driverRaceResultRepository.findAll();
    }
    // Lấy kết quả đua theo RaceStage
    public List<DriverRaceResult> getDriverRaceResultsByRaceStage(RaceStage raceStage) {
        return driverRaceResultRepository.findByRaceStage(raceStage);
    }
    
    // Lấy kết quả đua theo id stage đua
    public List<DriverRaceResult> getDriverRaceResultsByRaceStageId(String raceStageId) {
        return driverRaceResultRepository.findByRaceStageId(raceStageId);
    }
    // Lấy kết quả đua theo id tay đua và id mùa giải
    public List<DriverRaceResult> getDriverRaceResultsByDriverIdAndSeasonId(String driverId, String seasonId) {
        return driverRaceResultRepository.findByDriverIdAndSeasonId(driverId, seasonId);
    }
    
    // Lấy kết quả đua theo tay đua và mùa giải(theo hướng đối tượng)
    public List<DriverRaceResult> getDriverRaceResultsByDriverAndSeason(Driver driver, Season season) {
        return driverRaceResultRepository.findByDriverAndSeason(driver, season);
    }
    
    /**
     * Phương thức mới cập nhật kết quả chặng đua theo nguyên tắc hướng đối tượng
     * Implements Observer pattern - đây là phương thức "notifyObservers"
     */
    @Transactional
    public List<DriverRaceResult> updateRaceResults(List<DriverRaceResult> results) {
        if (results.isEmpty()) {
            return Collections.emptyList();
        }
        
        // Danh sách kết quả đã lưu
        List<DriverRaceResult> savedResults = new ArrayList<>();
        
        // Lấy thông tin chặng đua từ kết quả đầu tiên
        RaceStage raceStage = results.get(0).getRaceStage();
        
        // Đảm bảo các kết quả đều thuộc cùng một chặng đua
        for (DriverRaceResult result : results) {
            if (!result.getRaceStage().getId().equals(raceStage.getId())) {
                throw new IllegalArgumentException("Tất cả kết quả phải thuộc cùng một chặng đua");
            }
        }
        
        // Lấy ID của chặng đua và mùa giải
        String raceStageId = raceStage.getId();
        String seasonId = raceStage.getSeason().getId();
        
        // Lấy các kết quả hiện có theo race stage id
        List<DriverRaceResult> existingResults = driverRaceResultRepository.findByRaceStageId(raceStageId);
        Map<String, DriverRaceResult> existingResultsMap = new HashMap<>();
        
        // Tạo map với key là driverTeamAssignment_id để dễ dàng tìm kiếm
        for (DriverRaceResult existingResult : existingResults) {
            existingResultsMap.put(existingResult.getDriverTeamAssignment().getId(), existingResult);
        }
        
        // Danh sách các kết quả FINISHED để sắp xếp lại finish position nếu cần
        List<DriverRaceResult> finishedResults = new ArrayList<>();
        
        // Xử lý từng kết quả
        for (DriverRaceResult result : results) {
            // Đảm bảo các entity reference được đồng bộ
            syncEntityReferences(result);
            
            // Kiểm tra xem đã có kết quả cho assignment này chưa
            DriverRaceResult existingResult = existingResultsMap.get(result.getDriverTeamAssignment().getId());
            
            if (existingResult != null) {
                // Cập nhật kết quả hiện có
                existingResult.setGridPosition(result.getGridPosition());
                existingResult.setFinishPosition(result.getFinishPosition());
                existingResult.setStatus(result.getStatus());
                existingResult.setFinishTimeOrGap(result.getFinishTimeOrGap());
                existingResult.setLapsCompleted(result.getLapsCompleted());
                
                if (existingResult.getStatus() == RaceStatus.FINISHED) {
                    finishedResults.add(existingResult);
                }
                
                // Xóa khỏi map để biết kết quả nào đã được cập nhật
                existingResultsMap.remove(result.getDriverTeamAssignment().getId());
            } else {
                // Tạo mới kết quả nếu chưa tồn tại
                if (result.getId() == null) {
                    result.setId(UUID.randomUUID().toString());
                }
                
                if (result.getStatus() == RaceStatus.FINISHED) {
                    finishedResults.add(result);
                }
            }
        }
        
        // Sắp xếp lại các kết quả FINISHED theo thời gian hoàn thành nếu có
        if (!finishedResults.isEmpty()) {
            // Sắp xếp dựa trên thời gian hoàn thành
            finishedResults.sort((r1, r2) -> {
                String time1 = r1.getFinishTimeOrGap();
                String time2 = r2.getFinishTimeOrGap();
                
                if (time1 != null && time2 != null) {
                    // Nếu là thời gian đầy đủ (định dạng hh:mm:ss.ms)
                    if (time1.contains(":") && time2.contains(":")) {
                        // Chuyển đổi thời gian sang milliseconds để so sánh
                        try {
                            long ms1 = parseTimeToMillis(time1);
                            long ms2 = parseTimeToMillis(time2);
                            return Long.compare(ms1, ms2);
                        } catch (Exception e) {
                            // Nếu không parse được, thì so sánh chuỗi
                            return time1.compareTo(time2);
                        }
                    } 
                } else if (time1 != null) {
                    return -1; // r1 có thời gian, r2 không => r1 lên trước
                } else if (time2 != null) {
                    return 1;  // r2 có thời gian, r1 không => r2 lên trước
                }
                return 0; // Cả hai đều không có thời gian
            });
            
            // Cập nhật finishPosition dựa trên thứ tự mới
            for (int i = 0; i < finishedResults.size(); i++) {
                finishedResults.get(i).setFinishPosition(i + 1);
            }
        }
        
        // Tính điểm và lưu kết quả
        for (DriverRaceResult result : finishedResults) {
            // Tính điểm bằng Strategy pattern
            result.setPoints(pointsCalculationStrategy.calculatePoints(result));
            
            // Lưu kết quả
            savedResults.add(driverRaceResultRepository.save(result));
        }
        
        // Lưu các kết quả không phải FINISHED
        for (DriverRaceResult result : results) {
            if (result.getStatus() != RaceStatus.FINISHED) {
                result.setPoints(0); // Không phải FINISHED -> không có điểm
                savedResults.add(driverRaceResultRepository.save(result));
            }
        }
        
        // Xóa các kết quả không còn được sử dụng nữa
        if (!existingResultsMap.isEmpty()) {
            driverRaceResultRepository.deleteAll(existingResultsMap.values());
        }
        
        // Notify observers (StandingsService) through RabbitMQ
        RaceResultUpdatedEvent event = new RaceResultUpdatedEvent();
        event.setRaceStageId(raceStageId);
        event.setSeasonId(seasonId);
        event.setUpdatedAt(LocalDateTime.now());
        
        // Use Observer pattern to notify StandingsService
        rabbitTemplate.convertAndSend("f1-exchange", "race.results.updated", event);
        
        return savedResults;
    }
    
    /**
     * Đồng bộ các entity tham chiếu từ các service khác
     */
    private void syncEntityReferences(DriverRaceResult result) {
        // Đồng bộ DriverTeamAssignment
        DriverTeamAssignment driverTeamAssignment = result.getDriverTeamAssignment();
        DriverTeamAssignment existingAssignment = driverTeamAssignmentRepository.findById(driverTeamAssignment.getId()).orElse(null);
        
        if (existingAssignment == null) {
            // Đồng bộ Driver và Team trước khi lưu assignment
            syncDriver(driverTeamAssignment.getDriver());
            syncTeam(driverTeamAssignment.getTeam());
            
            // Lưu assignment mới
            driverTeamAssignmentRepository.save(driverTeamAssignment);
        } else {
            // Sử dụng assignment đã có
            result.setDriverTeamAssignment(existingAssignment);
        }
        
        // Đồng bộ RaceStage
        RaceStage raceStage = result.getRaceStage();
        RaceStage existingRaceStage = raceStageRepository.findById(raceStage.getId()).orElse(null);
        
        if (existingRaceStage == null) {
            // Kiểm tra và đồng bộ season trước khi lưu race stage
            Season season = raceStage.getSeason();
            if (season != null) {
                Season existingSeason = seasonRepository.findById(season.getId()).orElse(null);
                if (existingSeason != null) {
                    raceStage.setSeason(existingSeason);
                } else {
                    seasonRepository.save(season);
                }
            }
            // Lưu race stage mới
            raceStageRepository.save(raceStage);
        } else {
            // Sử dụng race stage đã có
            result.setRaceStage(existingRaceStage);
            // Đảm bảo sử dụng season của race stage hiện có
            result.setSeason(existingRaceStage.getSeason());
        }
    }
    
    /**
     * Đồng bộ Driver entity
     */
    private void syncDriver(Driver driver) {
        Driver existingDriver = driverRepository.findById(driver.getId()).orElse(null);
        
        if (existingDriver == null) {
            // Lấy thông tin tay đua từ participant-service
            try {
                Driver driverData = participantClient.getDriverById(driver.getId());
                if (driverData != null) {
                    driver.setFullName(driverData.getFullName());
                    driver.setNationality(driverData.getNationality());
                }
                driverRepository.save(driver);
            } catch (Exception e) {
                // Giữ nguyên thông tin hiện có và lưu
                driverRepository.save(driver);
            }
        }
    }
    
    /**
     * Đồng bộ Team entity
     */
    private void syncTeam(Team team) {
        Team existingTeam = teamRepository.findById(team.getId()).orElse(null);
        
        if (existingTeam == null) {
            // Lấy thông tin đội đua từ participant-service
            try {
                Team teamData = participantClient.getTeamById(team.getId());
                if (teamData != null) {
                    team.setName(teamData.getName());
                    team.setCountry(teamData.getCountry());
                    team.setBase(teamData.getBase());
                }
                teamRepository.save(team);
            } catch (Exception e) {
                // Giữ nguyên thông tin hiện có và lưu
                teamRepository.save(team);
            }
        }
    }
    
    // Parse thời gian từ chuỗi định dạng "hh:mm:ss.ms" sang milliseconds
    private long parseTimeToMillis(String timeString) {
        // Xử lý định dạng h:mm:ss.ms hoặc hh:mm:ss.ms
        String[] parts = timeString.split(":");
        long hours = 0;
        long minutes = 0;
        double seconds = 0;
        
        if (parts.length == 3) {
            hours = Long.parseLong(parts[0]);
            minutes = Long.parseLong(parts[1]);
            seconds = Double.parseDouble(parts[2]);
        } else if (parts.length == 2) {
            minutes = Long.parseLong(parts[0]);
            seconds = Double.parseDouble(parts[1]);
        } else {
            throw new IllegalArgumentException("Invalid time format: " + timeString);
        }
        
        return hours * 3600000 + minutes * 60000 + (long)(seconds * 1000);
    }
}