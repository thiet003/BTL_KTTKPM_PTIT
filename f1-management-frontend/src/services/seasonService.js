import api from './api';
// Lấy tất cả mùa giải
export const getAllSeasons = async () => {
  try {
    const response = await api.get('/seasons');
    return response.data;
  } catch (error) {
    console.error('Error fetching seasons:', error);
    throw error;
  }
};
// Lấy mùa giải theo id
export const getSeasonById = async (id) => {
  try {
    const response = await api.get(`/seasons/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching season with id ${id}:`, error);
    throw error;
  }
};
