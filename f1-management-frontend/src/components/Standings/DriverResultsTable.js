import React from 'react';
import { Table } from 'react-bootstrap';

const DriverResultsTable = ({ results }) => {
  // Sort results by race date if available
  const sortedResults = [...results].sort((a, b) => {
    if (a.raceDate && b.raceDate) {
      return new Date(a.raceDate) - new Date(b.raceDate);
    }
    return 0;
  });

  return (
    <Table striped bordered hover className="f1-table">
      <thead>
        <tr>
          <th>Chặng đua</th>
          <th>Vị trí về đích</th>
          <th>Điểm</th>
          <th>Trạng thái</th>
          <th>Thời gian/Khoảng cách</th>
        </tr>
      </thead>
      <tbody>
        {sortedResults.map((result) => (
          <tr key={result.id}>
            <td title={`ID: ${result.raceStageId}`}>{result.raceStage.name || result.raceStage.id}</td>
            <td>{result.finishPosition || 'N/A'}</td>
            <td>{result.points || 'N/A'}</td>
            <td>{translateStatus(result.status)}</td>
            <td>{result.finishTimeOrGap || 'N/A'}</td>
          </tr>
        ))}
      </tbody>
    </Table>
  );
};

// Helper function to translate race status to Vietnamese
const translateStatus = (status) => {
  const statusMap = {
    'PENDING': 'Chưa xuất phát',
    'FINISHED': 'Hoàn thành',
    'DISQUALIFIED': 'Không hoàn thành'
  };
  
  return statusMap[status] || status;
};

export default DriverResultsTable;
