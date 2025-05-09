import React from 'react';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import Navigation from './components/common/Navigation';
import HomePage from './pages/HomePage';
import RaceManagementPage from './pages/RaceManagementPage';
import RaceEditPage from './pages/RaceEditPage';
import RaceResultsPage from './pages/RaceResultsPage';
import RaceResultsEntryPage from './pages/RaceResultsEntryPage';
import StandingsPage from './pages/StandingsPage';
import DriverDetailsPage from './pages/DriverDetailsPage';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Navigation />
        <Container className="app-container">
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/race-management" element={<RaceManagementPage />} />
            <Route path="/race-management/edit/:id" element={<RaceEditPage />} />
            <Route path="/race-results" element={<RaceResultsPage />} />
            <Route path="/race-results/entry/:id" element={<RaceResultsEntryPage />} />
            <Route path="/standings" element={<StandingsPage />} />
            <Route path="/standings/driver/:id" element={<DriverDetailsPage />} />
          </Routes>
        </Container>
      </div>
    </Router>
  );
}

export default App;
