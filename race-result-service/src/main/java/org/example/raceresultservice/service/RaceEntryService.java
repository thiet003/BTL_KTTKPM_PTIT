package org.example.raceresultservice.service;

import org.example.raceresultservice.model.DriverTeamAssignment;
import org.example.raceresultservice.model.RaceEntry;
import org.example.raceresultservice.model.RaceStage;
import org.example.raceresultservice.repository.RaceEntryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RaceEntryService {

    @Autowired
    private RaceEntryRepository raceEntryRepository;
    // Lấy tất cả kết quả đua
    public List<RaceEntry> getAllRaceEntries() {
        return raceEntryRepository.findAll();
    }
    // Lấy kết quả đua theo id stage đua
    public List<RaceEntry> getRaceEntriesByRaceStageId(String raceStageId) {
        return raceEntryRepository.findByRaceStageId(raceStageId);
    }
    // Lấy kết quả đua theo RaceStage
    public List<RaceEntry> getRaceEntriesByRaceStage(RaceStage raceStage) {
        return raceEntryRepository.findByRaceStage(raceStage);
    }

}