package org.example.racescheduleservice.service;

import org.example.racescheduleservice.model.Season;
import org.example.racescheduleservice.repository.SeasonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SeasonService {

    @Autowired
    private SeasonRepository seasonRepository;

    // Lấy tất cả mùa giải
    public List<Season> getAllSeasons() {
        return seasonRepository.findAll();
    }

    // Lấy mùa giải theo id
    public Optional<Season> getSeasonById(String id) {
        return seasonRepository.findById(id);
    }
}
