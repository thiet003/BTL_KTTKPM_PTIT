import api from './api';
// Tìm kiếm chặng đua
export const searchRaces = async (keyword) => {
  try {
    const response = await api.get(`/race-stages/search?keyword=${keyword}`);
    return response.data;
  } catch (error) {
    console.error('Error searching races:', error);
    throw error;
  }
};
// Lấy chặng đua theo id
export const getRaceById = async (id) => {
  try {
    const response = await api.get(`/race-stages/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching race with id ${id}:`, error);
    throw error;
  }
};
// Cập nhật chặng đua
export const updateRace = async (id, raceData) => {
  try {
    const response = await api.put(`/race-stages/${id}`, raceData);
    return response.data;
  } catch (error) {
    console.error(`Error updating race with id ${id}:`, error);
    throw error;
  }
};
// Lấy tất cả chặng đua
export const getAllRaces = async () => {
  try {
    const response = await api.get('/race-stages');
    return response.data;
  } catch (error) {
    console.error('Error fetching all races:', error);
    throw error;
  }
};
// Lấy tất cả chặng đua theo id mùa giải
export const getRacesBySeasonId = async (seasonId) => {
  try {
    const response = await api.get(`/race-stages/season/${seasonId}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching races for season ${seasonId}:`, error);
    throw error;
  }
};
