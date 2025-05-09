import React, { useState, useEffect } from 'react';
import { Container, Form, Button, Spinner, Alert } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { getAllSeasons } from '../services/seasonService';
import RaceStageList from '../components/RaceResults/RaceStageList';
import { getRacesBySeasonId } from '../services/raceService';
// Trang chọn mùa giải và chặng đua
const RaceResultsPage = () => {
  const [seasons, setSeasons] = useState([]);
  const [selectedSeason, setSelectedSeason] = useState('');
  const [races, setRaces] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const navigate = useNavigate();

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
      fetchRaces(selectedSeason);
    }
  }, [selectedSeason]);

  const fetchRaces = async (seasonId) => {
    setLoading(true);
    try {
      const data = await getRacesBySeasonId(seasonId);
      setRaces(data);
    } catch (err) {
      setError('Không thể tải danh sách chặng đua. Vui lòng thử lại sau.');
      console.error('Error fetching races:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSeasonChange = (e) => {
    setSelectedSeason(e.target.value);
  };

  const handleContinue = () => {
    if (selectedSeason) {
      fetchRaces(selectedSeason);
    }
  };

  const handleRaceSelect = (race) => {
    navigate(`/race-results/entry/${race.id}`, { state: { seasonId: selectedSeason } });
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
      <h2 className="page-title">Cập nhật kết quả chặng đua</h2>
      
      {error && <Alert variant="danger">{error}</Alert>}
      
      <div className="mb-4">
        <h4>Chọn mùa giải</h4>
        <Form>
          <Form.Group className="mb-3">
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
          
          <Button 
            variant="primary" 
            className="f1-primary-btn"
            onClick={handleContinue}
            disabled={loading || !selectedSeason}
          >
            Tiếp tục
          </Button>
        </Form>
      </div>
      
      {loading && races.length === 0 ? (
        <Spinner animation="border" role="status">
          <span className="visually-hidden">Đang tải danh sách chặng đua...</span>
        </Spinner>
      ) : (
        selectedSeason && races.length > 0 && (
          <RaceStageList races={races} onRaceSelect={handleRaceSelect} />
        )
      )}
    </Container>
  );
};

export default RaceResultsPage;
