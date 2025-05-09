package org.example.participantservice.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamInfoUpdatedEvent {
    private String eventType = "TEAM_INFO_UPDATED";
    private String teamId;
    private String name;
    private LocalDateTime updatedAt;
}