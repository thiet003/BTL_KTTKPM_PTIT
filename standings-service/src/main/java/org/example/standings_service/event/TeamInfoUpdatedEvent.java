package org.example.standings_service.event;


import lombok.Data;

import java.time.LocalDateTime;

/**
 * Event cho thông tin đội
 */
@Data
public class TeamInfoUpdatedEvent {
    private String eventType;
    private String teamId;
    private String name;
    private LocalDateTime updatedAt;
}