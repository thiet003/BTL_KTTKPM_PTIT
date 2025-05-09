import React, { useState, useEffect } from 'react';
import { Container, Form, Button, Alert, Spinner, Modal } from 'react-bootstrap';
import { useParams, useNavigate } from 'react-router-dom';
import { getRaceById, updateRace } from '../services/raceService';
import { getAllCircuits } from '../services/circuitService';

const RaceEditPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const [race, setRace] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [formData, setFormData] = useState({
    name: '',
    laps: 0,
    circuitId: '',
    status: 'NOT_STARTED',
    description: ''
  });
  const [circuits, setCircuits] = useState([]);
  const [saveSuccess, setSaveSuccess] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Fetch circuits first
        const circuitsData = await getAllCircuits();
        setCircuits(circuitsData);
        
        // Then fetch race data
        const raceData = await getRaceById(id);
        setRace(raceData);
        
        console.log(raceData);
        
        // Determine circuit ID
        let circuitId = '';
        
        // Nếu API trả về circuitId trực tiếp (từ phiên bản mới)
        if (raceData.circuitId) {
          circuitId = raceData.circuitId;
        }
        // Nếu không có circuitId trực tiếp, sử dụng phương pháp cũ
        else if (raceData.circuit && raceData.circuit.id) {
          circuitId = raceData.circuit.id;
        }

        // Set form data
        setFormData({
          name: raceData.name || '',
          laps: raceData.laps || 0,
          circuitId: circuitId,
          status: raceData.status || 'NOT_STARTED',
          description: raceData.description || ''
        });
      } catch (err) {
        setError('Không thể tải thông tin chặng đua. Vui lòng thử lại sau.');
        console.error('Error fetching data:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchData();
  }, [id]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData({
      ...formData,
      [name]: value
    });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setSaveSuccess(false);

    try {
      // Prepare data for API
      const updateData = {
        ...race,
        name: formData.name,
        laps: parseInt(formData.laps, 10),
        status: formData.status,
        description: formData.description,
        circuit: {
          id: formData.circuitId
        }
      };

      // Xóa circuitId nếu có - để tránh lỗi unrecognized field
      if (updateData.circuitId) {
        delete updateData.circuitId;
      }

      await updateRace(id, updateData);
      setSaveSuccess(true);
    } catch (err) {
      setError('Có lỗi xảy ra khi lưu thông tin. Vui lòng thử lại sau.');
      console.error('Error updating race:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleConfirm = () => {
    navigate('/race-management');
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
        <Button variant="secondary" onClick={() => navigate('/race-management')}>
          Quay lại
        </Button>
      </Container>
    );
  }

  return (
    <Container className="form-container">
      <h2 className="page-title">Chỉnh sửa thông tin chặng đua</h2>

      {/* Modal xác nhận cập nhật thành công */}
      <Modal show={saveSuccess} onHide={handleConfirm} centered>
        <Modal.Header closeButton>
          <Modal.Title>Thông báo</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="text-center">
            <i className="bi bi-check-circle text-success" style={{ fontSize: '4rem' }}></i>
            <h4 className="mt-3">Thành công!</h4>
            <p>Thông tin chặng đua đã được cập nhật.</p>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="primary" onClick={handleConfirm}>
            OK
          </Button>
        </Modal.Footer>
      </Modal>

      {error && <Alert variant="danger">{error}</Alert>}

      <Form onSubmit={handleSubmit}>
        <Form.Group className="mb-3">
          <Form.Label>ID</Form.Label>
          <Form.Control type="text" value={id} disabled />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Tên chặng đua</Form.Label>
          <Form.Control
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            required
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Số vòng đua</Form.Label>
          <Form.Control
            type="number"
            name="laps"
            value={formData.laps}
            onChange={handleChange}
            required
            min="1"
          />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Đường đua</Form.Label>
          <Form.Select
            name="circuitId"
            value={formData.circuitId}
            onChange={handleChange}
            required
          >
            <option value="">-- Chọn đường đua --</option>
            {circuits.map(circuit => (
              <option key={circuit.id} value={circuit.id}>
                {circuit.name}, {circuit.country}
              </option>
            ))}
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Trạng thái</Form.Label>
          <Form.Select
            name="status"
            value={formData.status}
            onChange={handleChange}
            required
          >
            <option value="NOT_STARTED">Chưa diễn ra</option>
            <option value="ONGOING">Đang diễn ra</option>
            <option value="FINISHED">Đã kết thúc</option>
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Mô tả</Form.Label>
          <Form.Control
            as="textarea"
            rows={3}
            name="description"
            value={formData.description}
            onChange={handleChange}
            placeholder="Nhập thông tin mô tả về chặng đua"
          />
        </Form.Group>

        <Button
          variant="primary"
          type="submit"
          className="f1-primary-btn"
          disabled={loading}
        >
          {loading ? 'Đang lưu...' : 'Lưu'}
        </Button>{' '}
        <Button
          variant="secondary"
          onClick={() => navigate('/race-management')}
        >
          Hủy
        </Button>
      </Form>
    </Container>
  );
};

export default RaceEditPage;
