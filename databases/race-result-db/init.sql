CREATE DATABASE IF NOT EXISTS race_result_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE race_result_db;

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- Create drivers table (renamed from driver_refs)
CREATE TABLE IF NOT EXISTS driver_refs (
  id VARCHAR(36) PRIMARY KEY,
  full_name VARCHAR(255),
  nationality VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create teams table (renamed from team_refs)
CREATE TABLE IF NOT EXISTS team_refs (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(255),
  country VARCHAR(100),
  base VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create seasons_ref table (for reference in queries)
CREATE TABLE IF NOT EXISTS seasons_ref (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create driver_team_assignments_ref table
CREATE TABLE IF NOT EXISTS driver_team_assignments_ref (
  id VARCHAR(36) PRIMARY KEY,
  driver_id VARCHAR(36) NOT NULL,
  team_id VARCHAR(36) NOT NULL,
  start_date DATE,
  end_date DATE,
  FOREIGN KEY (driver_id) REFERENCES driver_refs(id),
  FOREIGN KEY (team_id) REFERENCES team_refs(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create race_stages_refs table (renamed from race_stage_refs)
CREATE TABLE IF NOT EXISTS race_stages_refs (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(255),
  race_date DATE,
  laps INT,
  circuit_name VARCHAR(255),
  season_id VARCHAR(36) NOT NULL,
  FOREIGN KEY (season_id) REFERENCES seasons_ref(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create race_entries table with proper object relationships
CREATE TABLE IF NOT EXISTS race_entries (
  id VARCHAR(36) PRIMARY KEY,
  race_stage_id VARCHAR(36) NOT NULL,
  driver_team_assignment_id VARCHAR(36) NOT NULL,
  car_number INT,
  entry_status VARCHAR(20) NOT NULL,
  registered_at DATETIME,
  UNIQUE KEY unique_race_driver (race_stage_id, driver_team_assignment_id),
  FOREIGN KEY (race_stage_id) REFERENCES race_stages_refs(id),
  FOREIGN KEY (driver_team_assignment_id) REFERENCES driver_team_assignments_ref(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create driver_race_results table with foreign key references
CREATE TABLE IF NOT EXISTS driver_race_results (
  id VARCHAR(36) PRIMARY KEY,
  race_stage_id VARCHAR(36) NOT NULL,
  driver_team_assignment_id VARCHAR(36) NOT NULL,
  season_id VARCHAR(36) NOT NULL,
  grid_position INT,
  finish_position INT,
  points INT,
  status VARCHAR(20) NOT NULL DEFAULT 'FINISHED',
  finish_time_or_gap VARCHAR(100),
  laps_completed INT,
  UNIQUE KEY unique_race_driver_result (race_stage_id, driver_team_assignment_id),
  FOREIGN KEY (race_stage_id) REFERENCES race_stages_refs(id),
  FOREIGN KEY (driver_team_assignment_id) REFERENCES driver_team_assignments_ref(id),
  FOREIGN KEY (season_id) REFERENCES seasons_ref(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert reference data for seasons
INSERT INTO seasons_ref (id, name) VALUES
('2024', 'Formula 1 2024 Season'),
('2023', 'Formula 1 2023 Season'),
('2022', 'Formula 1 2022 Season');

-- Insert reference data for drivers
INSERT INTO driver_refs (id, full_name, nationality) VALUES
('TD01', 'Nguyễn Văn An', 'Vietnam'),
('TD02', 'Lê Thị Hồng', 'Vietnam'),
('TD03', 'Bùi Trí Công', 'Vietnam'),
('TD04', 'Trần Văn Hoàng', 'Vietnam'),
('TD05', 'Lê Văn Hữu', 'Vietnam'),
('TD06', 'Mai Văn Sơn', 'Vietnam'),
('TDJS', 'John Smith', 'UK'),
('TDCG', 'Carlos Garcia', 'Spain'),
('TDMD', 'Michel Dubois', 'France'),
('TDTY', 'Takeshi Yamada', 'Japan'),
('TDNVA', 'Nguyễn Văn A', 'Vietnam'),
('TDAR', 'Antonio Rodriguez', 'Spain'),
('TDJP', 'James Parker', 'USA'),
('TDRL', 'Ricardo Lopez', 'Mexico'),
('TDSC', 'Sarah Chen', 'China'),
('TDPK', 'Pavel Kowalski', 'Poland'),
('TDHM', 'Hans Mueller', 'Germany'),
('TDLT', 'Laura Thompson', 'UK'),
('TDAK', 'Alexei Kozlov', 'Russia'),
('TDJT', 'Jack Thompson', 'Australia');

-- Insert reference data for teams
INSERT INTO team_refs (id, name, country, base) VALUES
('DR001', 'Dragon Racing', 'Vietnam', 'Hanoi'),
('TB001', 'Thunder Bolts', 'Vietnam', 'Ho Chi Minh City'),
('PF001', 'Phoenix Flyers', 'Vietnam', 'Da Nang'),
('TA001', 'Team Alpha', 'UK', 'London'),
('SR001', 'Speed Racers', 'Italy', 'Milan'),
('FW001', 'Fast Wheels', 'France', 'Paris'),
('SR002', 'Samurai Racing', 'Japan', 'Tokyo'),
('SW001', 'Speed Warriors', 'China', 'Shanghai'),
('LP001', 'Lightning Panthers', 'USA', 'New York'),
('GD001', 'Golden Dragons', 'China', 'Beijing'),
('RF001', 'Red Foxes', 'Germany', 'Berlin'),
('BS001', 'Blue Stars', 'UK', 'Manchester'),
('ES001', 'Eagle Squadron', 'USA', 'Los Angeles'),
('SC001', 'Silver Comets', 'France', 'Lyon');

-- Insert driver team assignments data
INSERT INTO driver_team_assignments_ref (id, driver_id, team_id, start_date, end_date) VALUES
-- 2024 Season assignments
('DTA001', 'TD01', 'DR001', '2024-01-01', '2024-12-31'),
('DTA002', 'TD02', 'TB001', '2024-01-01', '2024-12-31'),
('DTA003', 'TD03', 'TB001', '2024-01-01', '2024-12-31'),
('DTA004', 'TD04', 'PF001', '2024-01-01', '2024-12-31'),
('DTA005', 'TD05', 'DR001', '2024-01-01', '2024-12-31'),
('DTA006', 'TD06', 'PF001', '2024-01-01', '2024-12-31'),
('DTA007', 'TDJS', 'TA001', '2024-01-01', '2024-12-31'),
('DTA008', 'TDCG', 'SR001', '2024-01-01', '2024-12-31'),
('DTA009', 'TDMD', 'FW001', '2024-01-01', '2024-12-31'),
('DTA010', 'TDTY', 'SR002', '2024-01-01', '2024-12-31'),
('DTA011', 'TDNVA', 'SW001', '2024-01-01', '2024-12-31'),
('DTA012', 'TDAR', 'SR001', '2024-01-01', '2024-12-31'),
('DTA013', 'TDJP', 'TA001', '2024-01-01', '2024-12-31'),
('DTA014', 'TDRL', 'LP001', '2024-01-01', '2024-12-31'),
('DTA015', 'TDSC', 'GD001', '2024-01-01', '2024-12-31'),
('DTA016', 'TDPK', 'TB001', '2024-01-01', '2024-12-31'),
('DTA017', 'TDHM', 'RF001', '2024-01-01', '2024-12-31'),
('DTA018', 'TDLT', 'BS001', '2024-01-01', '2024-12-31'),
('DTA019', 'TDAK', 'RF001', '2024-01-01', '2024-12-31'),
('DTA020', 'TDJT', 'ES001', '2024-01-01', '2024-12-31'),

-- 2023 Season assignments
('DTA021', 'TD01', 'RF001', '2023-01-01', '2023-12-31'),
('DTA022', 'TD02', 'TA001', '2023-01-01', '2023-12-31'),
('DTA023', 'TD03', 'SR001', '2023-01-01', '2023-12-31'),
('DTA024', 'TD04', 'FW001', '2023-01-01', '2023-12-31'),
('DTA025', 'TD05', 'SR002', '2023-01-01', '2023-12-31'),
('DTA026', 'TD06', 'SW001', '2023-01-01', '2023-12-31'),
('DTA027', 'TDJS', 'BS001', '2023-01-01', '2023-12-31'),
('DTA028', 'TDCG', 'GD001', '2023-01-01', '2023-12-31'),
('DTA029', 'TDMD', 'SC001', '2023-01-01', '2023-12-31'),
('DTA030', 'TDTY', 'ES001', '2023-01-01', '2023-12-31'),
('DTA031', 'TDNVA', 'LP001', '2023-01-01', '2023-12-31'),

-- 2022 Season assignments
('DTA032', 'TDHM', 'SW001', '2022-01-01', '2022-12-31'),
('DTA033', 'TDSC', 'TB001', '2022-01-01', '2022-12-31'),
('DTA034', 'TDAR', 'FW001', '2022-01-01', '2022-12-31'),
('DTA035', 'TDJP', 'PF001', '2022-01-01', '2022-12-31'),
('DTA036', 'TDLT', 'DR001', '2022-01-01', '2022-12-31');

-- Insert reference data for race stages
INSERT INTO race_stages_refs (id, name, race_date, laps, circuit_name, season_id) VALUES
('GP01', 'Italian Grand Prix 2024', '2024-09-01', 53, 'Monza', '2024'),
('GP03', 'Spanish Grand Prix 2024', '2024-06-23', 66, 'Circuit de Barcelona-Catalunya', '2024'),
('GP05', 'Monaco Grand Prix 2024', '2024-05-26', 78, 'Circuit de Monaco', '2024'),
('GP10', 'Monaco Grand Prix 2023', '2023-05-28', 78, 'Circuit de Monaco', '2023'),
('GP11', 'British Grand Prix 2023', '2023-07-15', 52, 'Silverstone Circuit', '2023'),
('GP17', 'Italian Grand Prix 2022', '2022-09-11', 53, 'Monza', '2022');

-- Insert sample race entries for 2024 season
INSERT INTO race_entries (id, race_stage_id, driver_team_assignment_id, car_number, entry_status, registered_at) VALUES
-- Italian Grand Prix 2024 entries
('RE001', 'GP01', 'DTA001', 1, 'CONFIRMED', '2024-08-25 10:00:00'),
('RE002', 'GP01', 'DTA002', 2, 'CONFIRMED', '2024-08-25 10:15:00'),
('RE003', 'GP01', 'DTA003', 3, 'CONFIRMED', '2024-08-25 10:30:00'),
('RE004', 'GP01', 'DTA004', 4, 'CONFIRMED', '2024-08-25 10:45:00'),
('RE005', 'GP01', 'DTA005', 5, 'CONFIRMED', '2024-08-25 11:00:00'),
('RE006', 'GP01', 'DTA006', 6, 'CONFIRMED', '2024-08-25 11:15:00'),
('RE007', 'GP01', 'DTA007', 7, 'CONFIRMED', '2024-08-25 12:00:00'),
('RE008', 'GP01', 'DTA008', 8, 'CONFIRMED', '2024-08-25 12:15:00'),
('RE009', 'GP01', 'DTA009', 9, 'CONFIRMED', '2024-08-25 12:30:00'),
('RE010', 'GP01', 'DTA010', 10, 'CONFIRMED', '2024-08-25 12:45:00'),

-- Spanish Grand Prix 2024 entries
('RE011', 'GP03', 'DTA007', 7, 'CONFIRMED', '2024-08-01 09:00:00'),
('RE012', 'GP03', 'DTA008', 8, 'CONFIRMED', '2024-08-01 09:15:00'),
('RE013', 'GP03', 'DTA009', 9, 'CONFIRMED', '2024-08-01 09:30:00'),
('RE014', 'GP03', 'DTA010', 10, 'CONFIRMED', '2024-08-01 09:45:00'),
('RE015', 'GP03', 'DTA011', 11, 'CONFIRMED', '2024-08-01 10:00:00'),
('RE016', 'GP03', 'DTA012', 12, 'CONFIRMED', '2024-08-01 10:15:00'),
('RE017', 'GP03', 'DTA013', 14, 'CONFIRMED', '2024-08-01 10:30:00'),
('RE018', 'GP03', 'DTA014', 15, 'CONFIRMED', '2024-08-01 10:45:00'),
('RE019', 'GP03', 'DTA015', 16, 'CONFIRMED', '2024-08-01 11:00:00'),
('RE020', 'GP03', 'DTA016', 17, 'CONFIRMED', '2024-08-01 11:15:00'),

-- Monaco Grand Prix 2024 entries
('RE021', 'GP05', 'DTA001', 1, 'CONFIRMED', '2024-05-20 09:00:00'),
('RE022', 'GP05', 'DTA003', 3, 'CONFIRMED', '2024-05-20 09:15:00'),
('RE023', 'GP05', 'DTA005', 5, 'CONFIRMED', '2024-05-20 09:30:00'),
('RE024', 'GP05', 'DTA007', 7, 'CONFIRMED', '2024-05-20 09:45:00'),
('RE025', 'GP05', 'DTA009', 9, 'CONFIRMED', '2024-05-20 10:00:00'),
('RE026', 'GP05', 'DTA011', 11, 'CONFIRMED', '2024-05-20 10:15:00'),
('RE027', 'GP05', 'DTA017', 13, 'CONFIRMED', '2024-05-20 10:30:00'),
('RE028', 'GP05', 'DTA018', 18, 'CONFIRMED', '2024-05-20 10:45:00'),
('RE029', 'GP05', 'DTA019', 20, 'CONFIRMED', '2024-05-20 11:00:00'),
('RE030', 'GP05', 'DTA020', 23, 'CONFIRMED', '2024-05-20 11:15:00'),

-- 2023 Season entries
-- Monaco Grand Prix 2023
('RE031', 'GP10', 'DTA021', 1, 'CONFIRMED', '2023-05-21 09:00:00'),
('RE032', 'GP10', 'DTA022', 2, 'CONFIRMED', '2023-05-21 09:15:00'),
('RE033', 'GP10', 'DTA023', 3, 'CONFIRMED', '2023-05-21 09:30:00'),
('RE034', 'GP10', 'DTA024', 4, 'CONFIRMED', '2023-05-21 09:45:00'),
('RE035', 'GP10', 'DTA025', 5, 'CONFIRMED', '2023-05-21 10:00:00'),
('RE036', 'GP10', 'DTA026', 6, 'CONFIRMED', '2023-05-21 10:15:00'),
('RE037', 'GP10', 'DTA027', 7, 'CONFIRMED', '2023-05-21 10:30:00'),
('RE038', 'GP10', 'DTA028', 8, 'CONFIRMED', '2023-05-21 10:45:00'),

-- British Grand Prix 2023
('RE039', 'GP11', 'DTA021', 1, 'CONFIRMED', '2023-07-10 09:00:00'),
('RE040', 'GP11', 'DTA022', 2, 'CONFIRMED', '2023-07-10 09:15:00'),
('RE041', 'GP11', 'DTA023', 3, 'CONFIRMED', '2023-07-10 09:30:00'),
('RE042', 'GP11', 'DTA024', 4, 'CONFIRMED', '2023-07-10 09:45:00'),
('RE043', 'GP11', 'DTA025', 5, 'CONFIRMED', '2023-07-10 10:00:00'),
('RE044', 'GP11', 'DTA026', 6, 'CONFIRMED', '2023-07-10 10:15:00'),
('RE045', 'GP11', 'DTA027', 7, 'CONFIRMED', '2023-07-10 10:30:00'),

-- 2022 Season entries
-- Italian Grand Prix 2022
('RE046', 'GP17', 'DTA032', 13, 'CONFIRMED', '2022-09-04 09:00:00'),
('RE047', 'GP17', 'DTA033', 16, 'CONFIRMED', '2022-09-04 09:15:00'),
('RE048', 'GP17', 'DTA034', 12, 'CONFIRMED', '2022-09-04 09:30:00'),
('RE049', 'GP17', 'DTA035', 14, 'CONFIRMED', '2022-09-04 09:45:00'),
('RE050', 'GP17', 'DTA036', 18, 'CONFIRMED', '2022-09-04 10:00:00');

-- Insert sample race results for completed races
-- Italian Grand Prix 2024 results (GP01)
INSERT INTO driver_race_results (id, race_stage_id, driver_team_assignment_id, season_id, status, laps_completed) VALUES
('RR001', 'GP01', 'DTA001', '2024', 'PENDING', 0),
('RR002', 'GP01', 'DTA002', '2024', 'PENDING', 0),
('RR003', 'GP01', 'DTA003', '2024', 'PENDING', 0),
('RR004', 'GP01', 'DTA004', '2024', 'PENDING', 0),
('RR005', 'GP01', 'DTA005', '2024', 'PENDING', 0),
('RR006', 'GP01', 'DTA006', '2024', 'PENDING', 0),
('RR007', 'GP01', 'DTA007', '2024', 'PENDING', 0),
('RR008', 'GP01', 'DTA008', '2024', 'PENDING', 0),
('RR009', 'GP01', 'DTA009', '2024', 'PENDING', 0),
('RR010', 'GP01', 'DTA010', '2024', 'PENDING', 0),

-- Spanish Grand Prix 2024 results (GP03)
('RR011', 'GP03', 'DTA007', '2024', 'PENDING', 0),
('RR012', 'GP03', 'DTA008', '2024', 'PENDING', 0),
('RR013', 'GP03', 'DTA009', '2024', 'PENDING', 0),
('RR014', 'GP03', 'DTA010', '2024', 'PENDING', 0),
('RR015', 'GP03', 'DTA011', '2024', 'PENDING', 0),
('RR016', 'GP03', 'DTA012', '2024', 'PENDING', 0),
('RR017', 'GP03', 'DTA013', '2024', 'PENDING', 0),
('RR018', 'GP03', 'DTA014', '2024', 'PENDING', 0),
('RR019', 'GP03', 'DTA015', '2024', 'PENDING', 0),
('RR020', 'GP03', 'DTA016', '2024', 'PENDING', 0),

-- Monaco Grand Prix 2024 results (GP05)
('RR021', 'GP05', 'DTA001', '2024', 'PENDING', 0),
('RR022', 'GP05', 'DTA003', '2024', 'PENDING', 0),
('RR023', 'GP05', 'DTA005', '2024', 'PENDING', 0),
('RR024', 'GP05', 'DTA007', '2024', 'PENDING', 0),
('RR025', 'GP05', 'DTA009', '2024', 'PENDING', 0),
('RR026', 'GP05', 'DTA011', '2024', 'PENDING', 0),
('RR027', 'GP05', 'DTA017', '2024', 'PENDING', 0),
('RR028', 'GP05', 'DTA018', '2024', 'PENDING', 0),
('RR029', 'GP05', 'DTA019', '2024', 'PENDING', 0),
('RR030', 'GP05', 'DTA020', '2024', 'PENDING', 0),

-- Monaco Grand Prix 2023 results (GP10)
('RR031', 'GP10', 'DTA021', '2023', 'PENDING', 0),
('RR032', 'GP10', 'DTA022', '2023', 'PENDING', 0),
('RR033', 'GP10', 'DTA023', '2023', 'PENDING', 0),
('RR034', 'GP10', 'DTA024', '2023', 'PENDING', 0),
('RR035', 'GP10', 'DTA025', '2023', 'PENDING', 0),
('RR036', 'GP10', 'DTA026', '2023', 'PENDING', 0),
('RR037', 'GP10', 'DTA027', '2023', 'PENDING', 0),
('RR038', 'GP10', 'DTA028', '2023', 'PENDING', 0),

-- British Grand Prix 2023 results (GP11)
('RR039', 'GP11', 'DTA021', '2023', 'PENDING', 0),
('RR040', 'GP11', 'DTA022', '2023', 'PENDING', 0),
('RR041', 'GP11', 'DTA023', '2023', 'PENDING', 0),
('RR042', 'GP11', 'DTA024', '2023', 'PENDING', 0),
('RR043', 'GP11', 'DTA025', '2023', 'PENDING', 0),
('RR044', 'GP11', 'DTA026', '2023', 'PENDING', 0),
('RR045', 'GP11', 'DTA027', '2023', 'PENDING', 0),

-- Italian Grand Prix 2022 results (GP17)
('RR046', 'GP17', 'DTA032', '2022', 'PENDING', 0),
('RR047', 'GP17', 'DTA033', '2022', 'PENDING', 0),
('RR048', 'GP17', 'DTA034', '2022', 'PENDING', 0),
('RR049', 'GP17', 'DTA035', '2022', 'PENDING', 0),
('RR050', 'GP17', 'DTA036', '2022', 'PENDING', 0); 