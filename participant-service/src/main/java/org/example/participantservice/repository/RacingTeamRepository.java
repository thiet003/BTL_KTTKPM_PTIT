package org.example.participantservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.example.participantservice.model.RacingTeam;

@Repository
public interface RacingTeamRepository extends JpaRepository<RacingTeam, String> {
}