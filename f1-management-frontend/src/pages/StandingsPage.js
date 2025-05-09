import React, { useState, useEffect } from 'react';
import { Container, Form, Spinner, Alert } from 'react-bootstrap';
import { getAllSeasons } from '../services/seasonService';
import { getDriverStandingsBySeasonId } from '../services/standingsService';
import DriverStandingsTable from '../components/Standings/DriverStandingsTable';

const StandingsPage = () => {
  const [seasons, setSeasons] = useState([]);
  const [selectedSeason, setSelectedSeason] = useState('');
  const [standings, setStandings] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchSeasons = async () => {
      try {
        const data = await getAllSeasons();
        setSeasons(data);
        if (data.length > 0) {
          setSelectedSeason(data[0].id); // Select first season by default
        }
      } catch (err) {
        setError('Không thể tải danh sách mùa giải. Vui lòng thử lại sau.');
        console.error('Error fetching seasons:', err);
      } finally {
        setLoading(false);
      }
    };

    fetchSeasons();
  }, []);

  useEffect(() => {
    if (selectedSeason) {
      fetchStandings(selectedSeason);
    }
  }, [selectedSeason]);

  const fetchStandings = async (seasonId) => {
    setLoading(true);
    try {
      const data = await getDriverStandingsBySeasonId(seasonId);
      setStandings(data);
    } catch (err) {
      setError('Không thể tải bảng xếp hạng. Vui lòng thử lại sau.');
      console.error('Error fetching standings:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSeasonChange = (e) => {
    setSelectedSeason(e.target.value);
  };

  if (loading && seasons.length === 0) {
    return (
      <Container className="d-flex justify-content-center mt-5">
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Đang tải...</span>
        </Spinner>
      </Container>
    );
  }

  return (
    <Container>
      <h2 className="page-title">Bảng xếp hạng các tay đua</h2>
      
      {error && <Alert variant="danger">{error}</Alert>}
      
      <div className="mb-4">
        <Form>
          <Form.Group className="mb-3">
            <Form.Label>Chọn mùa giải</Form.Label>
            <Form.Select 
              value={selectedSeason} 
              onChange={handleSeasonChange}
              disabled={loading || seasons.length === 0}
            >
              {seasons.length === 0 ? (
                <option>Không có mùa giải nào</option>
              ) : (
                seasons.map(season => (
                  <option key={season.id} value={season.id}>
                    {season.name} ({season.year})
                  </option>
                ))
              )}
            </Form.Select>
          </Form.Group>
        </Form>
      </div>
      
      {loading && standings.length === 0 ? (
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Đang tải bảng xếp hạng...</span>
        </Spinner>
      ) : (
        <DriverStandingsTable 
          standings={standings} 
          seasonId={selectedSeason} 
        />
      )}
    </Container>
  );
};

export default StandingsPage;
