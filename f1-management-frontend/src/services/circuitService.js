import api from './api';
// Lấy tất cả các đường đua
export const getAllCircuits = async () => {
  try {
    const response = await api.get('/circuits');
    return response.data;
  } catch (error) {
    console.error('Error fetching circuits:', error);
    throw error;
  }
};

// Lấy đường đua theo id
export const getCircuitById = async (id) => {
  try {
    const response = await api.get(`/circuits/${id}`);
    return response.data;
  } catch (error) {
    console.error(`Error fetching circuit with id ${id}:`, error);
    throw error;
  }
};
