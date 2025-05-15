import React, { useState, useEffect } from 'react';
import { Container, Form, Button, Alert, Spinner, Card, Modal } from 'react-bootstrap';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { getRaceById } from '../services/raceService';
import { getRaceEntriesByRaceStageId, getRaceResultsByRaceStageId, updateRaceResults } from '../services/raceResultService';
import RaceResultsForm from '../components/RaceResults/RaceResultsForm';
import { v4 as uuidv4 } from 'uuid';
// Trang cập nhật kết quả chặng đua
const RaceResultsEntryPage = () => {
  const { id } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const seasonId = location.state?.seasonId;
  
  const [race, setRace] = useState(null);
  const [entries, setEntries] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [saveSuccess, setSaveSuccess] = useState(false);
  const [results, setResults] = useState([]);

  useEffect(() => {
    if (!seasonId) {
      setError('Không tìm thấy thông tin mùa giải. Vui lòng quay lại trang trước.');
      setLoading(false);
      return;
    }

    const fetchData = async () => {
      try {
        // Lấy thông tin chặng đua
        const raceData = await getRaceById(id);
        setRace(raceData);
        
        // Lấy thông tin đăng ký chặng đua
        const entriesData = await getRaceResultsByRaceStageId(id);
        setEntries(entriesData);
        
        // Lấy kết quả đua nếu có
        try {
          const existingResults = await getRaceResultsByRaceStageId(id);
          if (existingResults && existingResults.length > 0) {
            setResults(existingResults);
          } else {
            // Khởi tạo kết quả với giá trị mặc định nếu không có kết quả
            initializeDefaultResults(entriesData, raceData);
          }
        } catch (resultError) {
          console.log('Không tìm thấy kết quả, khởi tạo giá trị mặc định');
          // Khởi tạo kết quả với giá trị mặc định
          initializeDefaultResults(entriesData, raceData);
        }
      } catch (err) {
        setError('Không thể tải thông tin chặng đua. Vui lòng thử lại sau.');
        console.error('Error fetching data:', err);
      } finally {
        setLoading(false);
      }
    };

    const initializeDefaultResults = (entriesData, raceData) => {
      const initialResults = entriesData.map(entry => ({
        id: uuidv4(),
        raceStageId: id,
        driverId: entry.driverId,
        teamId: entry.teamId,
        driverName: entry.driverName || null,
        teamName: entry.teamName || null,
        gridPosition: null,
        finishPosition: null,
        points: 0,
        status: 'PENDING', // Default status
        finishTimeOrGap: '',
        lapsCompleted: raceData.laps // Default to full race distance
      }));
      
      setResults(initialResults);
    };

    fetchData();
  }, [id, seasonId]);

  const handleResultChange = (driverId, field, value) => {
    setResults(prevResults => {
      const updatedResults = prevResults.map(result => 
        result.driverId === driverId 
          ? { ...result, [field]: value } 
          : result
      );
      
      // Nếu đang thay đổi trạng thái thành FINISHED, tự động đặt vị trí về đích
      if (field === 'status' && value === 'FINISHED') {
        // Đếm số tay đua đã hoàn thành
        const finishedCount = updatedResults.filter(r => r.status === 'FINISHED').length;
        
        // Cập nhật vị trí về đích cho tay đua vừa hoàn thành
        return updatedResults.map(result => 
          result.driverId === driverId 
            ? { ...result, finishPosition: finishedCount } 
            : result
        );
      }
      
      return updatedResults;
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSaveSuccess(false);

    // Kiểm tra định dạng thời gian
    const isValidTimeFormat = (timeString) => {
      // Kiểm tra định dạng hh:mm:ss hoặc mm:ss
      const timePattern = /^(?:(\d+):)?(\d{1,2}):(\d{1,2})(?:\.(\d{1,3}))?$/;
      return timePattern.test(timeString);
    };

    // Kiểm tra tất cả tay đua FINISHED phải có thời gian hoàn thành và đúng định dạng
    const finishedDriversWithIssues = results.filter(
      result => result.status === 'FINISHED' && (
        !result.finishTimeOrGap || 
        result.finishTimeOrGap.trim() === '' || 
        !isValidTimeFormat(result.finishTimeOrGap)
      )
    );

    if (finishedDriversWithIssues.length > 0) {
      setError('Vui lòng nhập thời gian hoàn thành cho tất cả các tay đua có trạng thái "Hoàn thành" theo định dạng hh:mm:ss hoặc mm:ss.');
      setLoading(false);
      return;
    }

    // Kiểm tra số vòng hoàn thành phải hợp lệ
    const driversWithInvalidLaps = results.filter(result => {
      const lapsCompleted = parseInt(result.lapsCompleted, 10);
      const totalLaps = parseInt(race.laps, 10);
      
      if (result.status === 'PENDING') {
        // Tay đua chưa xuất phát phải có số vòng là 0
        return lapsCompleted !== 0;
      } else if (result.status === 'FINISHED') {
        // Tay đua hoàn thành phải chạy đủ số vòng
        return lapsCompleted !== totalLaps;
      } else {
        // Tay đua không hoàn thành không được chạy đủ số vòng
        return lapsCompleted >= totalLaps;
      }
    });

    if (driversWithInvalidLaps.length > 0) {
      setError('Số vòng hoàn thành không hợp lệ. Tay đua chưa xuất phát phải có số vòng là 0. Tay đua hoàn thành phải chạy đủ số vòng. Tay đua không hoàn thành không được chạy đủ số vòng.');
      setLoading(false);
      return;
    }

    try {
      // Process results before sending to API
      const processedResults = results.map(result => {
        // Tìm entry tương ứng để lấy thông tin tên nếu cần
        const entry = entries.find(e => e.driverId === result.driverId);
        
        return {
          ...result,
          driverName: result.driverName || (entry ? entry.driverName : null),
          teamName: result.teamName || (entry ? entry.teamName : null),
          gridPosition: result.gridPosition ? parseInt(result.gridPosition, 10) : null,
          finishPosition: result.status === 'FINISHED' ? parseInt(result.finishPosition, 10) : null,
          lapsCompleted: parseInt(result.lapsCompleted, 10),
          finishTimeOrGap: result.status === 'FINISHED' ? result.finishTimeOrGap : 'N/A'
        };
      });

      await updateRaceResults(id, seasonId, processedResults);
      setSaveSuccess(true);
    } catch (err) {
      setError('Có lỗi xảy ra khi lưu kết quả. Vui lòng thử lại sau.');
      console.error('Error updating race results:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleConfirm = () => {
    navigate('/race-results');
  };

  if (loading && !race) {
    return (
      <Container className="d-flex justify-content-center mt-5">
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Đang tải...</span>
        </Spinner>
      </Container>
    );
  }

  if (error && !race) {
    return (
      <Container>
        <Alert variant="danger">{error}</Alert>
        <Button variant="secondary" onClick={() => navigate('/race-results')}>
          Quay lại
        </Button>
      </Container>
    );
  }

  return (
    <Container>
      <h2 className="page-title">Cập nhật kết quả chặng đua</h2>
      
      {/* Modal xác nhận cập nhật thành công */}
      <Modal show={saveSuccess} onHide={handleConfirm} centered>
        <Modal.Header closeButton>
          <Modal.Title>Thông báo</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="text-center">
            <i className="bi bi-check-circle text-success" style={{ fontSize: '4rem' }}></i>
            <h4 className="mt-3">Thành công!</h4>
            <p>Kết quả chặng đua đã được cập nhật.</p>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={handleConfirm}>
            OK
          </Button>
        </Modal.Footer>
      </Modal>
      
      {error && <Alert variant="danger">{error}</Alert>}
      
      {race && (
        <Card className="mb-4">
          <Card.Body>
            <Card.Title>{race.name}</Card.Title>
            <Card.Text>
              <strong>Mã chặng đua:</strong> {race.id}<br />
              <strong>Số vòng đua:</strong> {race.laps}<br />
              <strong>Thời gian diễn ra:</strong> {new Date(race.raceDate).toLocaleDateString('vi-VN')}<br />
              <strong>Địa điểm:</strong> {race.circuit ? `${race.circuit.name}, ${race.circuit.country}` : 'N/A'}
            </Card.Text>
          </Card.Body>
        </Card>
      )}
      
      {entries.length > 0 ? (
        <Form onSubmit={handleSubmit}>
          <RaceResultsForm 
            entries={entries} 
            results={results} 
            onResultChange={handleResultChange}
            totalLaps={race?.laps || 0}
          />
          
          <Button 
            variant="primary" 
            type="submit" 
            className="f1-primary-btn mt-3"
            disabled={loading}
          >
            {loading ? 'Đang lưu...' : 'Lưu kết quả'}
          </Button>{' '}
          <Button 
            variant="secondary" 
            onClick={() => navigate('/race-results')}
            className="mt-3"
          >
            Hủy
          </Button>
        </Form>
      ) : (
        <Alert variant="info">
          Không có tay đua nào đăng ký tham gia chặng đua này.
        </Alert>
      )}
    </Container>
  );
};

export default RaceResultsEntryPage;
