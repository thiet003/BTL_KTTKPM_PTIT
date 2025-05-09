import React, { useEffect, useState } from 'react';
import { Table } from 'react-bootstrap';
import { getAllCircuits } from '../../services/circuitService';

const RaceList = ({ races, onRaceSelect }) => {
  const [circuits, setCircuits] = useState([]);

  useEffect(() => {
    // Tải danh sách tất cả đường đua
    const fetchCircuits = async () => {
      try {
        const data = await getAllCircuits();
        setCircuits(data);
      } catch (err) {
        console.error('Không thể tải thông tin đường đua:', err);
      }
    };

    fetchCircuits();
  }, []);

  if (!races || races.length === 0) {
    return <p>Không tìm thấy chặng đua nào.</p>;
  }

  // Hàm lấy thông tin địa điểm từ circuit hoặc circuitId
  const getLocation = (race) => {
    // Nếu có circuitId (phiên bản API mới)
    if (race.circuitId) {
      const circuit = circuits.find(c => c.id === race.circuitId);
      if (circuit) {
        return `${circuit.name}, ${circuit.country}`;
      }
    }
    
    // Nếu có đối tượng circuit đầy đủ
    if (race.circuit && race.circuit.name) {
      return `${race.circuit.name}, ${race.circuit.country}`;
    }
    
    // Trường hợp không có thông tin địa điểm
    return 'Chưa có thông tin';
  };

  return (
    <div>
      <h4>Danh sách chặng đua</h4>
      <Table striped bordered hover className="f1-table">
        <thead>
          <tr>
            <th>STT</th>
            <th>ID</th>
            <th>Tên chặng đua</th>
            <th>Số vòng đua</th>
            <th>Địa điểm</th>
            <th>Mô tả</th>
            <th>Trạng thái</th>
          </tr>
        </thead>
        <tbody>
          {races.map((race, index) => (
            <tr 
              key={race.id} 
              onClick={() => onRaceSelect(race)}
              className="clickable-row"
            >
              <td>{index + 1}</td>
              <td>{race.id}</td>
              <td>{race.name}</td>
              <td>{race.laps}</td>
              <td>{getLocation(race)}</td>
              <td>{getSummarizedDescription(race)}</td>
              <td>{translateStatus(race.status)}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

// Helper function to translate race status to Vietnamese
const translateStatus = (status) => {
  const statusMap = {
    'FINISHED': 'Đã kết thúc',
    'ONGOING': 'Đang diễn ra',
    'NOT_STARTED': 'Chưa diễn ra',
    'CANCELLED': 'Đã hủy'
  };
  
  return statusMap[status] || status;
};

// Helper function to get summarized description
const getSummarizedDescription = (race) => {
  if (!race.description) return 'Chưa có mô tả';
  
  // Show first 50 characters with ellipsis if longer
  return race.description.length > 50 
    ? `${race.description.substring(0, 50)}...` 
    : race.description;
};

export default RaceList;
