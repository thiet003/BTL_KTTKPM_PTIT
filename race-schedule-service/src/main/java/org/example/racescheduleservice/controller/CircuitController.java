package org.example.racescheduleservice.controller;

import org.example.racescheduleservice.model.Circuit;
import org.example.racescheduleservice.service.CircuitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
// Chỉ cần: lấy tất cả circuit, lấy circuit theo id
@RestController
@RequestMapping("/api/circuits")
public class CircuitController {

    @Autowired
    private CircuitService circuitService;

    @GetMapping
    // Lấy tất cả circuit
    public ResponseEntity<List<Circuit>> getAllCircuits() {
        return ResponseEntity.ok(circuitService.getAllCircuits());
    }

    @GetMapping("/{id}")
    // Lấy circuit theo id
    public ResponseEntity<Circuit> getCircuitById(@PathVariable String id) {
        return circuitService.getCircuitById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}