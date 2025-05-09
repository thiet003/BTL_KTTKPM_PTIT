import React, { useState, useEffect } from 'react';
import { Container, Card, Spinner, Alert, Button } from 'react-bootstrap';
import { useParams, useLocation, useNavigate } from 'react-router-dom';
import { getDriverRaceResults } from '../services/standingsService';
import DriverResultsTable from '../components/Standings/DriverResultsTable';

const DriverDetailsPage = () => {
  const { id: driverId } = useParams();
  const location = useLocation();
  const navigate = useNavigate();
  const { seasonId, driverName, teamName, nationality } = location.state || {};
  
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (!seasonId) {
      setError('Không tìm thấy thông tin mùa giải. Vui lòng quay lại trang trước.');
      setLoading(false);
      return;
    }

    const fetchResults = async () => {
      try {
        const data = await getDriverRaceResults(driverId, seasonId);
        setResults(data);
      } catch (err) {
        setError('Không thể tải kết quả thi đấu. Vui lòng thử lại sau.');
        console.error('Error fetching driver results:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchResults();
  }, [driverId, seasonId]);

  if (loading) {
    return (
      <Container className="d-flex justify-content-center mt-5">
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Đang tải...</span>
        </Spinner>
      </Container>
    );
  }

  if (error) {
    return (
      <Container>
        <Alert variant="danger">{error}</Alert>
        <Button variant="secondary" onClick={() => navigate('/standings')}>
          Quay lại
        </Button>
      </Container>
    );
  }

  return (
    <Container>
      <Button 
        variant="secondary" 
        onClick={() => navigate('/standings')}
        className="mb-3"
      >
        &lt; Quay lại bảng xếp hạng
      </Button>
      
      <Card className="mb-4">
        <Card.Body>
          <Card.Title>Thông tin tay đua</Card.Title>
          <Card.Text>
            <strong>Tên tay đua:</strong> {driverName || `Tay đua ${driverId}`}<br />
            <strong>Quốc tịch:</strong> {nationality || 'N/A'}<br />
            <strong>Đội đua:</strong> {teamName || 'N/A'}<br />
            <strong>ID:</strong> {driverId}
          </Card.Text>
        </Card.Body>
      </Card>
      
      <h3 className="mb-3">Kết quả các chặng đua</h3>
      
      {results.length > 0 ? (
        <DriverResultsTable results={results} />
      ) : (
        <Alert variant="info">
          Tay đua này chưa có kết quả thi đấu nào trong mùa giải hiện tại.
        </Alert>
      )}
    </Container>
  );
};

export default DriverDetailsPage;
