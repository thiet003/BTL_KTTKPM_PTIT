package org.example.racescheduleservice.service;

import org.example.racescheduleservice.model.RaceStage;
import org.example.racescheduleservice.repository.RaceStageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RaceStageService {

    @Autowired
    private RaceStageRepository raceStageRepository;

    // Lấy tất cả stage đua
    public List<RaceStage> getAllRaceStages() {
        return raceStageRepository.findAll();
    }

    // Lấy stage đua theo id
    public Optional<RaceStage> getRaceStageById(String id) {
        return raceStageRepository.findById(id);
    }

    // Lấy stage đua theo id mùa giải
    public List<RaceStage> getRaceStagesBySeasonId(String seasonId) {
        return raceStageRepository.findBySeasonId(seasonId);
    }

    // Tìm kiếm stage đua
    public List<RaceStage> searchRaceStages(String keyword) {
        return raceStageRepository.searchByKeyword(keyword);
    }

    // Cập nhật stage đua
    public RaceStage saveRaceStage(RaceStage raceStage) {
        return raceStageRepository.save(raceStage);
    }

}