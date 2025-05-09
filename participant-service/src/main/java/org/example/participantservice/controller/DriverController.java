package org.example.participantservice.controller;

import org.example.participantservice.model.Driver;
import org.example.participantservice.service.DriverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
// Trong chủ đề này, chỉ cần: lấy tất cả tay đua, lấy tay đua theo id, cập nhật tay đua
@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @GetMapping
    // Lấy tất cả tay đua
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }

    @GetMapping("/{id}")
    // Lấy tay đua theo id
    public ResponseEntity<Driver> getDriverById(@PathVariable String id) {
        return driverService.getDriverById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/bulk")
    // Lấy tay đua theo nhiều id
    public ResponseEntity<List<Driver>> getDriversByIds(@RequestParam Set<String> ids) {
        return ResponseEntity.ok(driverService.getDriversByIds(ids));
    }

    @PutMapping("/{id}")
    // Cập nhật tay đua
    public ResponseEntity<Driver> updateDriver(@PathVariable String id, @RequestBody Driver driver) {
        if (!driverService.getDriverById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        driver.setId(id);
        return ResponseEntity.ok(driverService.saveDriver(driver));
    }
}