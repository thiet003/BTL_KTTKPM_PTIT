package org.example.raceresultservice.controller;

import org.example.raceresultservice.model.Driver;
import org.example.raceresultservice.model.RaceEntry;
import org.example.raceresultservice.model.RaceStage;
import org.example.raceresultservice.model.Team;
import org.example.raceresultservice.service.RaceEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/race-entries")
public class RaceEntryController {

    @Autowired
    private RaceEntryService raceEntryService;

    @GetMapping
    // Lấy tất cả kết quả đăng ký chặng đua
    public ResponseEntity<List<RaceEntry>> getAllRaceEntries() {
        return ResponseEntity.ok(raceEntryService.getAllRaceEntries());
    }

    @PostMapping("/race-stage")
    // Lấy kết quả đăng ký chặng đua theo RaceStage
    public ResponseEntity<List<RaceEntry>> getRaceEntriesByRaceStage(@RequestBody RaceStage raceStage) {
        return ResponseEntity.ok(raceEntryService.getRaceEntriesByRaceStage(raceStage));
    }

    @GetMapping("/race-stage/{raceStageId}")
    // Lấy kết quả đăng ký chặng đua theo id stage đua
    public ResponseEntity<List<RaceEntry>> getRaceEntriesByRaceStageId(@PathVariable String raceStageId) {
        RaceStage raceStage = new RaceStage();
        raceStage.setId(raceStageId);
        return ResponseEntity.ok(raceEntryService.getRaceEntriesByRaceStage(raceStage));
    }
}