import api from './api';
// Lấy tất cả kết quả đăng ký chặng đua
export const getRaceEntriesByRaceStageId = async (raceStageId) => {
  try {
    const response = await api.get(`/race-entries/race-stage/${raceStageId}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching race entries for race stage ${raceStageId}:`, error);
    throw error;
  }
};

// Lấy tất cả kết quả đua theo đối tượng RaceStage 
export const getRaceResultsByRaceStage = async (raceStage) => {
  try {
    const response = await api.post('/race-results/race-stage', raceStage);
    return response.data;
  } catch (error) {
    console.error(`Error fetching race results for race stage ${raceStage.id}:`, error);
    throw error;
  }
};

// Giữ lại phương thức cũ để tương thích ngược
export const getRaceResultsByRaceStageId = async (raceStageId) => {
  try {
    const response = await api.get(`/race-results/race-stage/${raceStageId}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching race results for race stage ${raceStageId}:`, error);
    throw error;
  }
};

// Cập nhật kết quả đua - sử dụng phương thức theo hướng đối tượng
export const updateRaceResults = async (raceStageId, seasonId, results) => {
  try {
    // Chuyển đổi dữ liệu sang định dạng thích hợp với hướng đối tượng
    const raceStageRef = {
      id: raceStageId,
      season: {
        id: seasonId
      }
    };

    const convertedResults = results.map(result => {
      return {
        id: result.id,
        raceStage: raceStageRef,
        driverTeamAssignment: {
          id: result.driverTeamAssignmentId,
          driver: {
            id: result.driverId,
            fullName: result.driverName
          },
          team: {
            id: result.teamId,
            name: result.teamName
          }
        },
        season: {
          id: seasonId
        },
        gridPosition: result.gridPosition,
        finishPosition: result.finishPosition,
        points: result.points,
        status: result.status,
        finishTimeOrGap: result.finishTimeOrGap,
        lapsCompleted: result.lapsCompleted
      };
    });
    
    console.log('Sending race results with object structure:', convertedResults);
    
    // Dùng endpoint mới
    const response = await api.post(`/race-results/update`, convertedResults);
    return response.data;
  } catch (error) {
    console.error(`Error updating race results for race stage ${raceStageId}:`, error);
    throw error;
  }
};
