package org.example.racescheduleservice.service;

import org.example.racescheduleservice.model.Circuit;
import org.example.racescheduleservice.repository.CircuitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
// Chỉ cần: lấy tất cả circuit, lấy circuit theo id
@Service
public class CircuitService {

    @Autowired
    private CircuitRepository circuitRepository;

    public List<Circuit> getAllCircuits() {
        return circuitRepository.findAll();
    }

    public Optional<Circuit> getCircuitById(String id) {
        return circuitRepository.findById(id);
    }
}