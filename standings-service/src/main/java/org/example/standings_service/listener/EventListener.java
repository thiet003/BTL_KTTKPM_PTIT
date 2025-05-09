package org.example.standings_service.listener;

import lombok.extern.slf4j.Slf4j;
import org.example.standings_service.event.DriverInfoUpdatedEvent;
import org.example.standings_service.event.RaceResultUpdatedEvent;
import org.example.standings_service.event.TeamInfoUpdatedEvent;
import org.example.standings_service.service.DriverStandingService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Listener for events from other services (Observer pattern)
 */
@Component
@Slf4j
public class EventListener {

    @Autowired
    private DriverStandingService driverStandingService;

    // Lắng nghe cập nhật kết quả đua (Observer pattern)
    @RabbitListener(queues = "race-results-queue")
    public void handleRaceResultUpdated(RaceResultUpdatedEvent event) {
        log.info("Received race result updated event: {}", event);
        driverStandingService.recalculateStandingsAfterRaceUpdate(event.getRaceStageId(), event.getSeasonId());
    }

    // Lắng nghe cập nhật thông tin tay đua
    @RabbitListener(queues = "driver-info-queue")
    public void handleDriverInfoUpdated(DriverInfoUpdatedEvent event) {
        log.info("Received driver info updated event: {}", event);
        driverStandingService.updateDriverInfo(event.getDriverId(), event.getFullName(), event.getNationality());
    }

    // Lắng nghe cập nhật thông tin đội
    @RabbitListener(queues = "team-info-queue")
    public void handleTeamInfoUpdated(TeamInfoUpdatedEvent event) {
        log.info("Received team info updated event: {}", event);
        driverStandingService.updateTeamInfo(event.getTeamId(), event.getName());
    }
}