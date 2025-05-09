package org.example.racescheduleservice.controller;

import org.example.racescheduleservice.model.Season;
import org.example.racescheduleservice.service.SeasonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seasons")
public class SeasonController {

    @Autowired
    private SeasonService seasonService;

    // Lấy tất cả mùa giải
    @GetMapping
    public ResponseEntity<List<Season>> getAllSeasons() {
        return ResponseEntity.ok(seasonService.getAllSeasons());
    }

    @GetMapping("/{id}")
    // Lấy mùa giải theo id
    public ResponseEntity<Season> getSeasonById(@PathVariable String id) {
        return seasonService.getSeasonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
