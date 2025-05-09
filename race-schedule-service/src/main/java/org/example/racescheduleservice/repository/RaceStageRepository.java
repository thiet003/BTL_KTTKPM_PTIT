package org.example.racescheduleservice.repository;

import org.example.racescheduleservice.model.RaceStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RaceStageRepository extends JpaRepository<RaceStage, String> {
    List<RaceStage> findBySeasonId(String seasonId);

    @Query("SELECT r FROM RaceStage r WHERE r.name LIKE %:keyword% OR r.id LIKE %:keyword%")
    List<RaceStage> searchByKeyword(@Param("keyword") String keyword);
}