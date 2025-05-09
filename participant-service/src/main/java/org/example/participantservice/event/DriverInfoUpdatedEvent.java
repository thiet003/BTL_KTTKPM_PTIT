package org.example.participantservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverInfoUpdatedEvent {
    private String eventType = "DRIVER_INFO_UPDATED";
    private String driverId;
    private String fullName;
    private String nationality;
    private LocalDateTime updatedAt;
}