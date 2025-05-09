import React from 'react';
import { Table } from 'react-bootstrap';

const RaceStageList = ({ races, onRaceSelect }) => {
  if (!races || races.length === 0) {
    return <p>Không có chặng đua nào trong mùa giải này.</p>;
  }

  // Sort races by date
  const sortedRaces = [...races].sort((a, b) => {
    return new Date(a.raceDate) - new Date(b.raceDate);
  });

  return (
    <div>
      <h4>Danh sách chặng đua</h4>
      <Table striped bordered hover className="f1-table">
        <thead>
          <tr>
            <th>STT</th>
            <th>ID</th>
            <th>Tên chặng đua</th>
            <th>Thời gian diễn ra</th>
            <th>Trạng thái</th>
          </tr>
        </thead>
        <tbody>
          {sortedRaces.map((race, index) => (
            <tr 
              key={race.id} 
              onClick={() => onRaceSelect(race)}
              className={`clickable-row ${isRaceEditable(race.status) ? '' : 'text-muted'}`}
              title={isRaceEditable(race.status) ? 'Click để cập nhật kết quả' : 'Không thể cập nhật kết quả cho chặng đua chưa diễn ra'}
            >
              <td>{index + 1}</td>
              <td>{race.id}</td>
              <td>{race.name}</td>
              <td>{formatDate(race.raceDate)}</td>
              <td>{translateStatus(race.status)}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

// Helper function to format date
const formatDate = (dateString) => {
  if (!dateString) return 'N/A';
  const date = new Date(dateString);
  return date.toLocaleDateString('vi-VN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  });
};

// Helper function to translate race status to Vietnamese
const translateStatus = (status) => {
  const statusMap = {
    'FINISHED': 'Đã kết thúc',
    'ONGOING': 'Đang diễn ra',
    'NOT_STARTED': 'Chưa diễn ra',
    'CANCELLED': 'Đã hủy',
    'PENDING': 'Chưa xuất phát',
    'DISQUALIFIED': 'Không hoàn thành'
  };
  
  return statusMap[status] || status;
};

// Helper function to determine if race results can be edited
const isRaceEditable = (status) => {
  return status === 'FINISHED' || status === 'PENDING';
};

export default RaceStageList;
