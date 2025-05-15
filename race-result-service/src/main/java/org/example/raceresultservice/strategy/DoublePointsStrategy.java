package org.example.raceresultservice.strategy;
import org.example.raceresultservice.model.DriverRaceResult;
import org.example.raceresultservice.model.RaceStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * Cách tính điểm gấp đôi
 * Mỗi vị trí được nhân đôi điểm
 */
@Component
@Qualifier("double")
public class DoublePointsStrategy implements PointsCalculationStrategy {

    @Override
    public int calculatePoints(DriverRaceResult result) {
        if (result.getStatus() != RaceStatus.FINISHED) {
            return 0;
        }
        Integer position = result.getFinishPosition();
        if (position == null) {
            return 0;
        }
        
        // Tính điểm tiêu chuẩn rồi nhân 2
        int standardPoints = switch (position) {
            case 1 -> 25;
            case 2 -> 18;
            case 3 -> 15;
            case 4 -> 12;
            case 5 -> 10;
            case 6 -> 8;
            case 7 -> 6;
            case 8 -> 4;
            case 9 -> 2;
            case 10 -> 1;
            default -> 0;
        };
        
        return standardPoints * 2;
    }
}