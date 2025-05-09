import React from 'react';
import { Navbar, Nav, Container } from 'react-bootstrap';
import { Link, useLocation } from 'react-router-dom';

const Navigation = () => {
  const location = useLocation();

  return (
    <Navbar bg="dark" variant="dark" expand="lg">
      <Container>
        <Navbar.Brand as={Link} to="/">F1 Management System</Navbar.Brand>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <Nav.Link 
              as={Link} 
              to="/race-management"
              className={location.pathname.includes('/race-management') ? 'active-nav-link' : ''}
            >
              Quản lý chặng đua
            </Nav.Link>
            <Nav.Link 
              as={Link} 
              to="/race-results"
              className={location.pathname.includes('/race-results') ? 'active-nav-link' : ''}
            >
              Cập nhật kết quả
            </Nav.Link>
            <Nav.Link 
              as={Link} 
              to="/standings"
              className={location.pathname.includes('/standings') ? 'active-nav-link' : ''}
            >
              Bảng xếp hạng
            </Nav.Link>
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Navigation;
