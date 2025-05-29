import React from 'react';
import { Table } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';

const DriverStandingsTable = ({ standings, seasonId }) => {
  const navigate = useNavigate();

  if (!standings || standings.length === 0) {
    return <p>Không có dữ liệu bảng xếp hạng cho mùa giải này.</p>;
  }

  const handleDriverSelect = (driver) => {
    navigate(`/standings/driver/${driver.driver.id}`, { 
      state: { 
        seasonId: seasonId,
        driverName: driver.driver.fullName,
        teamName: driver.lastTeam.name,
        nationality: driver.driver.nationality
      } 
    });
  };

  return (
    <div>
      <Table striped bordered hover className="f1-table">
        <thead>
          <tr>
            <th>Thứ hạng</th>
            <th>Tay đua</th>
            <th>Quốc tịch</th>
            <th>Đội đua</th>
            <th>Điểm</th>
            <th>Số chiến thắng</th>
            <th>Số lần đứng podium</th>
          </tr>
        </thead>
        <tbody>
          {standings.map((standing) => (
            <tr 
              key={standing.id} 
              onClick={() => handleDriverSelect(standing)}
              className="clickable-row"
            >
              <td>{standing.rank}</td>
              <td title={`ID: ${standing.driver.id}`}>{standing.driver.fullName || standing.driver.id}</td>
              <td>{standing.driver.nationality || 'N/A'}</td>
              <td title={`ID: ${standing.lastTeam.id}`}>{standing.lastTeam.name || standing.lastTeam.id}</td>
              <td>{standing.totalPoints}</td>
              <td>{standing.wins}</td>
              <td>{standing.podiums}</td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
};

export default DriverStandingsTable;
