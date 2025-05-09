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
// Lấy tất cả kết quả đua theo id chặng đua
export const getRaceResultsByRaceStageId = async (raceStageId) => {
  try {
    const response = await api.get(`/race-results/race-stage/${raceStageId}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching race results for race stage ${raceStageId}:`, error);
    throw error;
  }
};
// Cập nhật kết quả đua
export const updateRaceResults = async (raceStageId, seasonId, results) => {
  try {
    console.log('Sending race results with driver/team names:', results);
    
    const response = await api.post(`/race-results/update/${raceStageId}?seasonId=${seasonId}`, results);
    return response.data;
  } catch (error) {
    console.error(`Error updating race results for race stage ${raceStageId}:`, error);
    throw error;
  }
};
