package org.example.standings_service.event;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Event cho thông tin tay đua
 */
@Data
public class DriverInfoUpdatedEvent {
    private String eventType;
    private String driverId;
    private String fullName;
    private String nationality;
    private LocalDateTime updatedAt;
}