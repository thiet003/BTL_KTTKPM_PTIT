import React, { useState } from 'react';
import { Form, Button, Row, Col } from 'react-bootstrap';
import { searchRaces } from '../../services/raceService';

const RaceSearch = ({ onSearchResults }) => {
  const [keyword, setKeyword] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSearch = async (e) => {
    e.preventDefault();
    if (!keyword.trim()) {
      return;
    }

    setIsLoading(true);
    setError(null);

    try {
      const results = await searchRaces(keyword);
      onSearchResults(results);
    } catch (err) {
      setError('Có lỗi xảy ra khi tìm kiếm. Vui lòng thử lại sau.');
      console.error('Search error:', err);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="search-container">
      <h4>Tìm kiếm chặng đua</h4>
      <Form onSubmit={handleSearch}>
        <Row>
          <Col md={8}>
            <Form.Group controlId="searchKeyword">
              <Form.Control
                type="text"
                placeholder="Nhập tên hoặc ID chặng đua"
                value={keyword}
                onChange={(e) => setKeyword(e.target.value)}
              />
            </Form.Group>
          </Col>
          <Col md={4}>
            <Button 
              variant="primary" 
              type="submit" 
              className="f1-primary-btn"
              disabled={isLoading}
            >
              {isLoading ? 'Đang tìm...' : 'Tìm kiếm'}
            </Button>
          </Col>
        </Row>
      </Form>
      {error && <div className="text-danger mt-2">{error}</div>}
    </div>
  );
};

export default RaceSearch;
