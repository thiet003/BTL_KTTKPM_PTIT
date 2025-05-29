package org.example.standings_service.repository;

import org.example.standings_service.model.RacingTeam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RacingTeamRepository extends JpaRepository<RacingTeam, String> {
} 