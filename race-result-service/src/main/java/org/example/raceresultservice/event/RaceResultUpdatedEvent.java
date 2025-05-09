package org.example.raceresultservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
// Event được phát ra khi kết quả đua được cập nhật (Observer pattern)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RaceResultUpdatedEvent {
    private String eventType = "RACE_RESULT_UPDATED";
    private String raceStageId;
    private String seasonId;
    private LocalDateTime updatedAt;
}
