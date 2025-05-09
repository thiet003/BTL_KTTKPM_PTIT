import React, { useState } from 'react';
import { Container, Row, Col, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import RaceSearch from '../components/RaceManagement/RaceSearch';
import RaceList from '../components/RaceManagement/RaceList';
import { getAllRaces } from '../services/raceService';
// Trang quản lý chặng đua
const RaceManagementPage = () => {
  const [races, setRaces] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [showSearchSection, setShowSearchSection] = useState(false);
  const navigate = useNavigate();
// Lấy tất cả chặng đua
  const fetchAllRaces = async () => {
    setLoading(true);
    try {
      const data = await getAllRaces();
      setRaces(data);
      setError(null);
    } catch (err) {
      setError('Không thể tải danh sách chặng đua. Vui lòng thử lại sau.');
      console.error('Error fetching races:', err);
    } finally {
      setLoading(false);
    }
  };
// Hiển thị phần tìm kiếm
  const handleEditClick = () => {
    setShowSearchSection(true);
    fetchAllRaces();
  };
// Hiển thị kết quả tìm kiếm
  const handleSearchResults = (results) => {
    setRaces(results);
  };
// Chọn chặng đua
  const handleRaceSelect = (race) => {
    navigate(`/race-management/edit/${race.id}`);
  };
// Thêm chặng đua
  const handleAddNew = () => {
    // Implement add new race functionality
    alert('Chức năng thêm mới chặng đua sẽ được phát triển sau');
  };
// Xóa chặng đua
  const handleDelete = () => {
    // Implement delete race functionality
    alert('Chức năng xóa chặng đua sẽ được phát triển sau');
  };
// Hiển thị tất cả chặng đua
  const handleResetSearch = () => {
    fetchAllRaces();
  };

  return (
    <Container>
      <h2 className="page-title">Quản lý thông tin chặng đua</h2>
      
      <Row className="action-buttons mb-3">
        <Col>
          <Button variant="primary" className="f1-primary-btn" onClick={handleAddNew}>
            Thêm mới chặng đua
          </Button>{' '}
          <Button 
            variant="primary" 
            className="f1-primary-btn" 
            onClick={handleEditClick}
          >
            Chỉnh sửa thông tin chặng đua
          </Button>{' '}
          <Button variant="danger" onClick={handleDelete}>
            Xóa chặng đua
          </Button>
        </Col>
      </Row>
      
      {showSearchSection && (
        <>
          <Row className="mb-4">
            <Col>
              <RaceSearch onSearchResults={handleSearchResults} />
              <Button 
                variant="secondary" 
                className="mt-2"
                onClick={handleResetSearch}
              >
                Hiển thị tất cả
              </Button>
            </Col>
          </Row>
          
          {loading ? (
            <p>Đang tải danh sách chặng đua...</p>
          ) : error ? (
            <div className="alert alert-danger">{error}</div>
          ) : (
            <RaceList races={races} onRaceSelect={handleRaceSelect} />
          )}
        </>
      )}
    </Container>
  );
};

export default RaceManagementPage;
