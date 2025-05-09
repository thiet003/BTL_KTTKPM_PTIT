package org.example.racescheduleservice.controller;

import org.example.racescheduleservice.model.RaceStage;
import org.example.racescheduleservice.service.RaceStageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/race-stages")
public class RaceStageController {

    @Autowired
    private RaceStageService raceStageService;

    @GetMapping
    // Lấy tất cả stage đua
    public ResponseEntity<List<RaceStage>> getAllRaceStages() {
        return ResponseEntity.ok(raceStageService.getAllRaceStages());
    }

    @GetMapping("/{id}")
    // Lấy stage đua theo id
    public ResponseEntity<RaceStage> getRaceStageById(@PathVariable String id) {
        return raceStageService.getRaceStageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/season/{seasonId}")
    // Lấy stage đua theo id mùa giải
    public ResponseEntity<List<RaceStage>> getRaceStagesBySeasonId(@PathVariable String seasonId) {
        return ResponseEntity.ok(raceStageService.getRaceStagesBySeasonId(seasonId));
    }

    @GetMapping("/search")
    // Tìm kiếm stage đua
    public ResponseEntity<List<RaceStage>> searchRaceStages(@RequestParam String keyword) {
        return ResponseEntity.ok(raceStageService.searchRaceStages(keyword));
    }

    @PutMapping("/{id}")
    // Cập nhật stage đua
    public ResponseEntity<RaceStage> updateRaceStage(@PathVariable String id, @RequestBody RaceStage raceStage) {
        return raceStageService.getRaceStageById(id)
                .map(existingRaceStage -> {
                    // Giữ nguyên các quan hệ nếu không được cung cấp trong request
                    if (raceStage.getCircuit() == null) {
                        raceStage.setCircuit(existingRaceStage.getCircuit());
                    }
                    if (raceStage.getSeason() == null) {
                        raceStage.setSeason(existingRaceStage.getSeason());
                    }

                    // Cập nhật các trường khác
                    raceStage.setId(id);
                    if (raceStage.getName() == null) {
                        raceStage.setName(existingRaceStage.getName());
                    }
                    if (raceStage.getRaceDate() == null) {
                        raceStage.setRaceDate(existingRaceStage.getRaceDate());
                    }
                    if (raceStage.getLaps() == null) {
                        raceStage.setLaps(existingRaceStage.getLaps());
                    }
                    if (raceStage.getStatus() == null) {
                        raceStage.setStatus(existingRaceStage.getStatus());
                    }
                    if (raceStage.getDescription() == null) {
                        raceStage.setDescription(existingRaceStage.getDescription());
                    }

                    return ResponseEntity.ok(raceStageService.saveRaceStage(raceStage));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}