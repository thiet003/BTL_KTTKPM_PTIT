package org.example.participantservice.service;

import org.example.participantservice.event.DriverInfoUpdatedEvent;
import org.example.participantservice.model.Driver;
import org.example.participantservice.repository.DriverRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class DriverService {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;
    // Lấy tất cả tay đua
    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Optional<Driver> getDriverById(String id) {
        return driverRepository.findById(id);
    }
    // Lấy tay đua theo nhiều id
    public List<Driver> getDriversByIds(Set<String> ids) {
        List<Driver> drivers = new ArrayList<>();
        driverRepository.findAllById(ids).forEach(drivers::add);
        return drivers;
    }

    public Driver saveDriver(Driver driver) {
        Driver savedDriver = driverRepository.save(driver);

        // Publish event when driver info is updated
        DriverInfoUpdatedEvent event = new DriverInfoUpdatedEvent();
        event.setDriverId(savedDriver.getId());
        event.setFullName(savedDriver.getFullName());
        event.setNationality(savedDriver.getNationality());
        event.setUpdatedAt(LocalDateTime.now());

        rabbitTemplate.convertAndSend("f1-exchange", "driver.info.updated", event);

        return savedDriver;
    }

}