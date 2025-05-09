package org.example.racescheduleservice.repository;

import org.example.racescheduleservice.model.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeasonRepository extends JpaRepository<Season, String> {
}
