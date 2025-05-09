package org.example.raceresultservice.controller;

import org.example.raceresultservice.model.RaceEntry;
import org.example.raceresultservice.service.RaceEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/{id}")
    // Lấy kết quả đăng ký chặng đua theo id
    public ResponseEntity<RaceEntry> getRaceEntryById(@PathVariable String id) {
        return raceEntryService.getRaceEntryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/race-stage/{raceStageId}")
    // Lấy kết quả đăng ký chặng đua theo id stage đua
    public ResponseEntity<List<RaceEntry>> getRaceEntriesByRaceStageId(@PathVariable String raceStageId) {
        return ResponseEntity.ok(raceEntryService.getRaceEntriesByRaceStageId(raceStageId));
    }
}