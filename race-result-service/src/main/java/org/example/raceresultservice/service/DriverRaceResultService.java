package org.example.raceresultservice.service;

import org.example.raceresultservice.event.RaceResultUpdatedEvent;
import org.example.raceresultservice.model.DriverRaceResult;
import org.example.raceresultservice.model.RaceStatus;
import org.example.raceresultservice.repository.DriverRaceResultRepository;
import org.example.raceresultservice.strategy.PointsCalculationStrategy;
import org.example.raceresultservice.client.ParticipantClient;
import org.example.raceresultservice.model.Driver;
import org.example.raceresultservice.model.RacingTeam;
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
    // Lấy tất cả kết quả đua
    public List<DriverRaceResult> getAllDriverRaceResults() {
        return driverRaceResultRepository.findAll();
    }
    // Lấy kết quả đua theo id
    public Optional<DriverRaceResult> getDriverRaceResultById(String id) {
        return driverRaceResultRepository.findById(id);
    }
    // Lấy kết quả đua theo id stage đua
    public List<DriverRaceResult> getDriverRaceResultsByRaceStageId(String raceStageId) {
        return driverRaceResultRepository.findByRaceStageId(raceStageId);
    }
    // Lấy kết quả đua theo id tay đua
    public List<DriverRaceResult> getDriverRaceResultsByDriverId(String driverId) {
        return driverRaceResultRepository.findByDriverId(driverId);
    }
    // Lấy kết quả đua theo id tay đua và id mùa giải
    public List<DriverRaceResult> getDriverRaceResultsByDriverIdAndSeasonId(String driverId, String seasonId) {
        return driverRaceResultRepository.findByDriverIdAndSeasonId(driverId, seasonId);
    }
    /**
     * Cập nhật kết quả chặng đua
     * Implements Observer pattern - đây là phương thức "notifyObservers"
     */
    @Transactional
    public List<DriverRaceResult> updateRaceResults(String raceStageId, List<DriverRaceResult> results, String seasonId) {
        // Danh sách kết quả đã lưu
        List<DriverRaceResult> savedResults = new ArrayList<>();
        
        // Lấy các kết quả hiện có theo race stage id để map nhanh sau này
        List<DriverRaceResult> existingResults = driverRaceResultRepository.findByRaceStageId(raceStageId);
        Map<String, DriverRaceResult> existingResultsMap = new HashMap<>();
        
        // Tạo map với key là driver_id để dễ dàng tìm kiếm
        for (DriverRaceResult existingResult : existingResults) {
            existingResultsMap.put(existingResult.getDriverId(), existingResult);
        }

        // Danh sách các kết quả FINISHED để sắp xếp lại finish position nếu cần
        List<DriverRaceResult> finishedResults = new ArrayList<>();

        // Bước 1: Xử lý tất cả kết quả trước
        for (DriverRaceResult result : results) {
            result.setRaceStageId(raceStageId);
            result.setSeasonId(seasonId);
            
            // Kiểm tra xem đã có kết quả cho tay đua này chưa
            DriverRaceResult existingResult = existingResultsMap.get(result.getDriverId());
            
            if (existingResult != null) {
                // Cập nhật kết quả hiện có - giữ nguyên ID
                existingResult.setGridPosition(result.getGridPosition());
                existingResult.setFinishPosition(result.getFinishPosition());
                existingResult.setStatus(result.getStatus());
                existingResult.setFinishTimeOrGap(result.getFinishTimeOrGap());
                existingResult.setLapsCompleted(result.getLapsCompleted());
                existingResult.setTeamId(result.getTeamId());
                existingResult.setSeasonId(seasonId);
                
                // Cập nhật tên đội nếu teamId thay đổi hoặc chưa có tên đội
                if (!existingResult.getTeamId().equals(result.getTeamId()) || existingResult.getTeamName() == null) {
                    try {
                        RacingTeam team = participantClient.getTeamById(result.getTeamId());
                        if (team != null) {
                            existingResult.setTeamName(team.getName());
                        }
                    } catch (Exception e) {
                        // Nếu không lấy được thông tin, giữ nguyên tên đội cũ hoặc dùng giá trị mới nếu có
                        if (result.getTeamName() != null) {
                            existingResult.setTeamName(result.getTeamName());
                        }
                    }
                }
                
                if (existingResult.getStatus() == RaceStatus.FINISHED) {
                    finishedResults.add(existingResult);
                }
                
                // Xóa khỏi map để biết kết quả nào đã được cập nhật
                existingResultsMap.remove(result.getDriverId());
            } else {
                // Tạo mới kết quả nếu chưa tồn tại
                result.setId(UUID.randomUUID().toString());
                
                // Lấy thông tin tay đua và đội nếu chưa có
                if (result.getDriverName() == null) {
                    try {
                        Driver driver = participantClient.getDriverById(result.getDriverId());
                        if (driver != null) {
                            result.setDriverName(driver.getFullName());
                        }
                    } catch (Exception e) {
                        // Bỏ qua lỗi, giữ nguyên null
                    }
                }
                
                if (result.getTeamName() == null) {
                    try {
                        RacingTeam team = participantClient.getTeamById(result.getTeamId());
                        if (team != null) {
                            result.setTeamName(team.getName());
                        }
                    } catch (Exception e) {
                        // Bỏ qua lỗi, giữ nguyên null
                    }
                }
                
                if (result.getStatus() == RaceStatus.FINISHED) {
                    finishedResults.add(result);
                }
            }
        }
        
        // Bước 2: Sắp xếp lại các kết quả FINISHED theo thời gian hoàn thành nếu có
        if (!finishedResults.isEmpty()) {
            // Sắp xếp dựa trên thời gian hoàn thành (chuỗi định dạng thời gian như 1:32:45.123)
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
        
        // Bước 3: Tính điểm và lưu kết quả
        for (DriverRaceResult existingResult : finishedResults) {
            // Tính điểm bằng Strategy pattern
            existingResult.setPoints(pointsCalculationStrategy.calculatePoints(existingResult));
            
            // Lưu kết quả
            savedResults.add(driverRaceResultRepository.save(existingResult));
        }
        
        // Lưu các kết quả không phải FINISHED
        for (DriverRaceResult result : results) {
            if (result.getStatus() != RaceStatus.FINISHED) {
                if (result.getId() == null) {
                    result.setId(UUID.randomUUID().toString());
                }
                result.setPoints(0); // Không phải FINISHED -> không có điểm
                savedResults.add(driverRaceResultRepository.save(result));
            }
        }
        
        // Xóa các kết quả không còn được sử dụng nữa (tùy chọn)
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
    
    // Tìm kết quả đua theo id stage đua và id tay đua
    public Optional<DriverRaceResult> findByRaceStageIdAndDriverId(String raceStageId, String driverId) {
        return driverRaceResultRepository.findByRaceStageIdAndDriverId(raceStageId, driverId);
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