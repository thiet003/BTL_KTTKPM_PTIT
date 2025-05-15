package org.example.raceresultservice.service;

import org.example.raceresultservice.model.Driver;
import org.example.raceresultservice.model.RaceEntry;
import org.example.raceresultservice.model.RaceStage;
import org.example.raceresultservice.model.Team;
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
    // Lấy kết quả đua theo id
    public Optional<RaceEntry> getRaceEntryById(String id) {
        return raceEntryRepository.findById(id);
    }
    // Lấy kết quả đua theo id stage đua
    public List<RaceEntry> getRaceEntriesByRaceStageId(String raceStageId) {
        return raceEntryRepository.findByRaceStageId(raceStageId);
    }
    // Lấy kết quả đua theo RaceStage
    public List<RaceEntry> getRaceEntriesByRaceStage(RaceStage raceStage) {
        return raceEntryRepository.findByRaceStage(raceStage);
    }
    // Lấy kết quả đua theo Driver
    public List<RaceEntry> getRaceEntriesByDriver(Driver driver) {
        return raceEntryRepository.findByDriver(driver);
    }
    // Lấy kết quả đua theo Team
    public List<RaceEntry> getRaceEntriesByTeam(Team team) {
        return raceEntryRepository.findByTeam(team);
    }
    // Lấy kết quả đua theo RaceStage và Driver
    public Optional<RaceEntry> getRaceEntryByRaceStageAndDriver(RaceStage raceStage, Driver driver) {
        List<RaceEntry> entries = raceEntryRepository.findByRaceStageAndDriver(raceStage, driver);
        return entries.isEmpty() ? Optional.empty() : Optional.of(entries.get(0));
    }
}