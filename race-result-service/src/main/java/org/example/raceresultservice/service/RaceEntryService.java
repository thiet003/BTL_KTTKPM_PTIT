package org.example.raceresultservice.service;

import org.example.raceresultservice.model.RaceEntry;
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
}