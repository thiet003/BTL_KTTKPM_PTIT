import React from 'react';
import { Table, Form, InputGroup } from 'react-bootstrap';

const RaceResultsForm = ({ results, onResultChange, totalLaps }) => {
  // Kiểm tra định dạng thời gian
  const isValidTimeFormat = (timeString) => {
    // Kiểm tra định dạng hh:mm:ss hoặc mm:ss
    const timePattern = /^(?:(\d+):)?(\d{1,2}):(\d{1,2})(?:\.(\d{1,3}))?$/;
    return timePattern.test(timeString);
  };
  
  const handleStatusChange = (driverId, value) => {
    onResultChange(driverId, 'status', value);
    
    // Chuyển đổi totalLaps sang số nguyên
    const totalLapsInt = parseInt(totalLaps, 10);
    
    if (value === 'PENDING') {
      // Nếu chưa xuất phát, clear finish position và set số vòng = 0
      onResultChange(driverId, 'finishPosition', null);
      onResultChange(driverId, 'finishTimeOrGap', 'N/A');
      onResultChange(driverId, 'lapsCompleted', 0);
    } else if (value !== 'FINISHED') {
      // Nếu không hoàn thành, clear finish position
      onResultChange(driverId, 'finishPosition', null);
      onResultChange(driverId, 'finishTimeOrGap', 'N/A');
      
      // Nếu không hoàn thành, số vòng không được bằng tổng số vòng
      const currentResult = results.find(r => r.driverId === driverId);
      const currentLaps = parseInt(currentResult?.lapsCompleted || 0, 10);
      
      if (currentLaps === totalLapsInt) {
        onResultChange(driverId, 'lapsCompleted', totalLapsInt - 1);
      }
    } else {
      // Khi chuyển sang FINISHED, yêu cầu nhập thời gian hoàn thành
      onResultChange(driverId, 'finishTimeOrGap', '');
      // Khi hoàn thành, số vòng phải bằng tổng số vòng
      onResultChange(driverId, 'lapsCompleted', totalLapsInt);
    }
  };
  
  // Xử lý khi thay đổi thời gian hoàn thành
  const handleTimeChange = (driverId, value) => {
    onResultChange(driverId, 'finishTimeOrGap', value);
  };
  
  // Xử lý khi thay đổi số vòng hoàn thành
  const handleLapsChange = (driverId, value, isFinished, status) => {
    const laps = parseInt(value, 10);
    const totalLapsInt = parseInt(totalLaps, 10);
    
    // Nếu status là PENDING, luôn đặt về 0
    if (status === 'PENDING') {
      onResultChange(driverId, 'lapsCompleted', 0);
      return;
    }
    
    // Kiểm tra tính hợp lệ của số vòng hoàn thành
    if (isFinished && laps < totalLapsInt) {
      // Nếu đã hoàn thành, số vòng phải bằng tổng số vòng
      onResultChange(driverId, 'lapsCompleted', totalLapsInt);
    } else if (!isFinished && laps === totalLapsInt) {
      // Nếu chưa hoàn thành, số vòng không được bằng tổng số vòng
      onResultChange(driverId, 'lapsCompleted', totalLapsInt - 1);
    } else {
      onResultChange(driverId, 'lapsCompleted', laps);
    }
  };

  return (
    <div>
      <h4>Kết quả thi đấu</h4>
      <div className="alert alert-info">
        <strong>Lưu ý:</strong>
        <ul>
          <li>Tay đua chưa xuất phát (PENDING) phải có số vòng hoàn thành là 0.</li>
          <li>Tay đua hoàn thành (FINISHED) phải có số vòng đua bằng tổng số vòng đua ({totalLaps}).</li>
          <li>Tay đua không hoàn thành (DISQUALIFIED) không được có số vòng đua bằng tổng số vòng đua.</li>
          <li>Đối với tay đua hoàn thành (FINISHED), cần nhập thời gian theo định dạng "hh:mm:ss" hoặc "mm:ss" (ví dụ: 1:32:45 hoặc 32:45).</li>
        </ul>
      </div>
      <Table striped bordered hover className="f1-table">
        <thead>
          <tr>
            <th>STT</th>
            <th>ID</th>
            <th>Họ và tên</th>
            <th>Đội đua</th>
            <th>Trạng thái</th>
            <th>Thời gian hoàn thành</th>
            <th>Số vòng hoàn thành</th>
          </tr>
        </thead>
        <tbody>
          {results.map((result, index) => {
            const isFinished = result.status === 'FINISHED';
            const isTimeValid = !isFinished || !result.finishTimeOrGap || isValidTimeFormat(result.finishTimeOrGap);
            
            // Chuyển đổi kiểu dữ liệu lúc so sánh
            const lapsCompleted = parseInt(result.lapsCompleted || 0, 10);
            const isPending = result.status === 'PENDING';
            const isLapsValid = 
              (isPending && lapsCompleted === 0) ||
              (isFinished && lapsCompleted === parseInt(totalLaps, 10)) || 
              (!isFinished && !isPending && lapsCompleted < parseInt(totalLaps, 10));
            
            return (
              <tr key={result.id}>
                <td>{index + 1}</td>
                <td>{result.driverId}</td>
                <td>{result.driverName || 'Tay đua ' + result.driverId}</td>
                <td>{result.teamName || 'Đội ' + result.teamId}</td>
                <td>
                  <Form.Select
                    value={result.status || 'PENDING'}
                    onChange={(e) => handleStatusChange(result.driverId, e.target.value)}
                  >
                    <option value="PENDING">Chưa xuất phát</option>
                    <option value="FINISHED">Hoàn thành</option>
                    <option value="DISQUALIFIED">Không hoàn thành</option>
                  </Form.Select>
                </td>
                <td>
                  <InputGroup>
                    <Form.Control
                      type="text"
                      placeholder={isFinished ? "1:32:45" : "N/A"}
                      value={result.finishTimeOrGap || ''}
                      onChange={(e) => handleTimeChange(result.driverId, e.target.value)}
                      disabled={!isFinished}
                      isInvalid={isFinished && (!result.finishTimeOrGap || !isTimeValid)}
                    />
                    {isFinished && !result.finishTimeOrGap && (
                      <Form.Control.Feedback type="invalid">
                        Cần nhập thời gian
                      </Form.Control.Feedback>
                    )}
                    {isFinished && result.finishTimeOrGap && !isTimeValid && (
                      <Form.Control.Feedback type="invalid">
                        Định dạng không hợp lệ. Sử dụng hh:mm:ss hoặc mm:ss
                      </Form.Control.Feedback>
                    )}
                  </InputGroup>
                </td>
                <td>
                  <InputGroup>
                    <Form.Control
                      type="number"
                      min="0"
                      max={totalLaps}
                      value={result.lapsCompleted || (isFinished ? totalLaps : 0)}
                      onChange={(e) => handleLapsChange(result.driverId, e.target.value, isFinished, result.status)}
                      isInvalid={!isLapsValid}
                    />
                    {!isLapsValid && (
                      <Form.Control.Feedback type="invalid">
                        {isPending 
                          ? "Tay đua chưa xuất phát phải có số vòng là 0"
                          : isFinished 
                            ? `Phải hoàn thành đủ ${totalLaps} vòng đua` 
                            : `Không thể hoàn thành đủ ${totalLaps} vòng đua`}
                      </Form.Control.Feedback>
                    )}
                  </InputGroup>
                </td>
              </tr>
            );
          })}
        </tbody>
      </Table>
    </div>
  );
};

export default RaceResultsForm;
