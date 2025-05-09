import React from 'react';
import { Container, Row, Col, Card, Button } from 'react-bootstrap';
import { Link } from 'react-router-dom';

const HomePage = () => {
  return (
    <Container>
      <Row className="mt-5">
        <Col>
          <h1 className="text-center mb-5">Hệ thống Quản lý Giải đua F1</h1>
        </Col>
      </Row>
      <Row>
        <Col md={4} className="mb-4">
          <Card className="h-100">
            <Card.Body>
              <Card.Title>Quản lý thông tin chặng đua</Card.Title>
              <Card.Text>
                Thêm, sửa, xóa và quản lý thông tin các chặng đua trong giải đua F1.
              </Card.Text>
              <Button as={Link} to="/race-management" variant="primary" className="f1-primary-btn">
                Truy cập
              </Button>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4} className="mb-4">
          <Card className="h-100">
            <Card.Body>
              <Card.Title>Cập nhật kết quả chặng đua</Card.Title>
              <Card.Text>
                Cập nhật kết quả của các tay đua sau mỗi chặng đua.
              </Card.Text>
              <Button as={Link} to="/race-results" variant="primary" className="f1-primary-btn">
                Truy cập
              </Button>
            </Card.Body>
          </Card>
        </Col>
        <Col md={4} className="mb-4">
          <Card className="h-100">
            <Card.Body>
              <Card.Title>Bảng xếp hạng</Card.Title>
              <Card.Text>
                Xem bảng xếp hạng các tay đua và thông tin chi tiết về thành tích.
              </Card.Text>
              <Button as={Link} to="/standings" variant="primary" className="f1-primary-btn">
                Truy cập
              </Button>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default HomePage;
