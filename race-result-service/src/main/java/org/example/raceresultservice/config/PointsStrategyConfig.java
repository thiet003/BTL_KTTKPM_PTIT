package org.example.raceresultservice.config;

import org.example.raceresultservice.strategy.PointsCalculationStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class PointsStrategyConfig {

    @Bean
    @Primary
    public PointsCalculationStrategy activePointsStrategy(
            @Qualifier("standard") PointsCalculationStrategy standardStrategy,
            @Qualifier("double") PointsCalculationStrategy doubleStrategy) {
        // Mặc định sử dụng standard strategy
        return standardStrategy;
        
        // Chuyển sang double strategy bằng cách comment dòng trên và bỏ comment dòng dưới
        // return doubleStrategy;
    }
}