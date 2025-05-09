package org.example.participantservice.service;

import org.example.participantservice.event.TeamInfoUpdatedEvent;
import org.example.participantservice.model.RacingTeam;
import org.example.participantservice.repository.RacingTeamRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class RacingTeamService {

    @Autowired
    private RacingTeamRepository racingTeamRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // Lấy tất cả đội   
    public List<RacingTeam> getAllTeams() {
        return racingTeamRepository.findAll();
    }

    // Lấy đội theo id
    public Optional<RacingTeam> getTeamById(String id) {
        return racingTeamRepository.findById(id);
    }

    // Lấy đội theo nhiều id
    public List<RacingTeam> getTeamsByIds(Set<String> ids) {
        List<RacingTeam> teams = new ArrayList<>();
        racingTeamRepository.findAllById(ids).forEach(teams::add);
        return teams;
    }

    // Cập nhật đội
    public RacingTeam saveTeam(RacingTeam team) {
        RacingTeam savedTeam = racingTeamRepository.save(team);

        // Publish event when team info is updated
        TeamInfoUpdatedEvent event = new TeamInfoUpdatedEvent();
        event.setTeamId(savedTeam.getId());
        event.setName(savedTeam.getName());
        event.setUpdatedAt(LocalDateTime.now());

        rabbitTemplate.convertAndSend("f1-exchange", "team.info.updated", event);

        return savedTeam;
    }
}