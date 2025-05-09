import api from './api';
// Lấy xếp hạng tay đua theo id mùa giải
export const getDriverStandingsBySeasonId = async (seasonId) => {
  try {
    const response = await api.get(`/driver-standings/season/${seasonId}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching driver standings for season ${seasonId}:`, error);
    throw error;
  }
};
// Lấy xếp hạng tay đua theo id mùa giải và id tay đua
export const getDriverStandingBySeasonIdAndDriverId = async (seasonId, driverId) => {
  try {
    const response = await api.get(`/driver-standings/season/${seasonId}/driver/${driverId}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching driver standing for season ${seasonId} and driver ${driverId}:`, error);
    throw error;
  }
};
// Lấy kết quả đua theo id tay đua và id mùa giải
export const getDriverRaceResults = async (driverId, seasonId) => {
  try {
    const response = await api.get(`/race-results/driver/${driverId}/season/${seasonId}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching race results for driver ${driverId} in season ${seasonId}:`, error);
    throw error;
  }
};
