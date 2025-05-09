package org.example.standings_service.event;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * Event cho kết quả đua đã được cập nhật (Observer pattern)
 */
@Data
public class RaceResultUpdatedEvent {
    private String eventType;
    private String raceStageId;
    private String seasonId;
    private LocalDateTime updatedAt;
}