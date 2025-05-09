package org.example.participantservice.controller;

import org.example.participantservice.model.RacingTeam;
import org.example.participantservice.service.RacingTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
// Trong chủ đề này, chỉ cần: lấy tất cả đội, lấy đội theo id, cập nhật đội
@RestController
@RequestMapping("/api/teams")
public class RacingTeamController {

    @Autowired
    private RacingTeamService racingTeamService;

    @GetMapping
    // Lấy tất cả đội
    public ResponseEntity<List<RacingTeam>> getAllTeams() {
        return ResponseEntity.ok(racingTeamService.getAllTeams());
    }

    @GetMapping("/{id}")
    // Lấy đội theo id
    public ResponseEntity<RacingTeam> getTeamById(@PathVariable String id) {
        return racingTeamService.getTeamById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bulk")
    public ResponseEntity<List<RacingTeam>> getTeamsByIds(@RequestParam Set<String> ids) {
        return ResponseEntity.ok(racingTeamService.getTeamsByIds(ids));
    }


    @PutMapping("/{id}")
    // Cập nhật đội
    public ResponseEntity<RacingTeam> updateTeam(@PathVariable String id, @RequestBody RacingTeam team) {
        if (!racingTeamService.getTeamById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        team.setId(id);
        return ResponseEntity.ok(racingTeamService.saveTeam(team));
    }
}
