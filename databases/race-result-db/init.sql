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
  driver_id VARCHAR(36) NOT NULL,
  team_id VARCHAR(36) NOT NULL,
  car_number INT,
  entry_status VARCHAR(20) NOT NULL,
  registered_at DATETIME,
  UNIQUE KEY unique_race_driver (race_stage_id, driver_id),
  FOREIGN KEY (race_stage_id) REFERENCES race_stages_refs(id),
  FOREIGN KEY (driver_id) REFERENCES driver_refs(id),
  FOREIGN KEY (team_id) REFERENCES team_refs(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create driver_race_results table with foreign key references
CREATE TABLE IF NOT EXISTS driver_race_results (
  id VARCHAR(36) PRIMARY KEY,
  race_stage_id VARCHAR(36) NOT NULL,
  driver_id VARCHAR(36) NOT NULL,
  team_id VARCHAR(36) NOT NULL,
  season_id VARCHAR(36) NOT NULL,
  grid_position INT,
  finish_position INT,
  points INT,
  status VARCHAR(20) NOT NULL DEFAULT 'FINISHED',
  finish_time_or_gap VARCHAR(100),
  laps_completed INT,
  UNIQUE KEY unique_race_driver_result (race_stage_id, driver_id),
  FOREIGN KEY (race_stage_id) REFERENCES race_stages_refs(id),
  FOREIGN KEY (driver_id) REFERENCES driver_refs(id),
  FOREIGN KEY (team_id) REFERENCES team_refs(id),
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
('ES001', 'Eagle Squadron', 'USA', 'Los Angeles');

-- Insert reference data for race stages
INSERT INTO race_stages_refs (id, name, race_date, laps, circuit_name, season_id) VALUES
('GP01', 'Italian Grand Prix 2024', '2024-09-01', 53, 'Monza', '2024'),
('GP03', 'Spanish Grand Prix 2024', '2024-06-23', 66, 'Circuit de Barcelona-Catalunya', '2024'),
('GP05', 'Monaco Grand Prix 2024', '2024-05-26', 78, 'Circuit de Monaco', '2024'),
('GP10', 'Monaco Grand Prix 2023', '2023-05-28', 78, 'Circuit de Monaco', '2023'),
('GP11', 'British Grand Prix 2023', '2023-07-15', 52, 'Silverstone Circuit', '2023'),
('GP17', 'Italian Grand Prix 2022', '2022-09-11', 53, 'Monza', '2022');

-- Insert sample race entries for 2024 season
INSERT INTO race_entries (id, race_stage_id, driver_id, team_id, car_number, entry_status, registered_at) VALUES
-- Italian Grand Prix 2024 entries
('RE001', 'GP01', 'TD01', 'DR001', 1, 'CONFIRMED', '2024-08-25 10:00:00'),
('RE002', 'GP01', 'TD02', 'TB001', 2, 'CONFIRMED', '2024-08-25 10:15:00'),
('RE003', 'GP01', 'TD03', 'TB001', 3, 'CONFIRMED', '2024-08-25 10:30:00'),
('RE004', 'GP01', 'TD04', 'PF001', 4, 'CONFIRMED', '2024-08-25 10:45:00'),
('RE005', 'GP01', 'TD05', 'DR001', 5, 'CONFIRMED', '2024-08-25 11:00:00'),
('RE006', 'GP01', 'TD06', 'PF001', 6, 'CONFIRMED', '2024-08-25 11:15:00'),
('RE007', 'GP01', 'TDJS', 'TA001', 7, 'CONFIRMED', '2024-08-25 12:00:00'),
('RE008', 'GP01', 'TDCG', 'SR001', 8, 'CONFIRMED', '2024-08-25 12:15:00'),
('RE009', 'GP01', 'TDMD', 'FW001', 9, 'CONFIRMED', '2024-08-25 12:30:00'),
('RE010', 'GP01', 'TDTY', 'SR002', 10, 'CONFIRMED', '2024-08-25 12:45:00'),

-- Spanish Grand Prix 2024 entries
('RE011', 'GP03', 'TDJS', 'TA001', 7, 'CONFIRMED', '2024-08-01 09:00:00'),
('RE012', 'GP03', 'TDCG', 'SR001', 8, 'CONFIRMED', '2024-08-01 09:15:00'),
('RE013', 'GP03', 'TDMD', 'FW001', 9, 'CONFIRMED', '2024-08-01 09:30:00'),
('RE014', 'GP03', 'TDTY', 'SR002', 10, 'CONFIRMED', '2024-08-01 09:45:00'),
('RE015', 'GP03', 'TDNVA', 'SW001', 11, 'CONFIRMED', '2024-08-01 10:00:00'),
('RE016', 'GP03', 'TDAR', 'SR001', 12, 'CONFIRMED', '2024-08-01 10:15:00'),
('RE017', 'GP03', 'TDJP', 'TA001', 14, 'CONFIRMED', '2024-08-01 10:30:00'),
('RE018', 'GP03', 'TDRL', 'LP001', 15, 'CONFIRMED', '2024-08-01 10:45:00'),
('RE019', 'GP03', 'TDSC', 'GD001', 16, 'CONFIRMED', '2024-08-01 11:00:00'),
('RE020', 'GP03', 'TDPK', 'TB001', 17, 'CONFIRMED', '2024-08-01 11:15:00'),

-- Monaco Grand Prix 2024 entries
('RE021', 'GP05', 'TD01', 'DR001', 1, 'CONFIRMED', '2024-05-20 09:00:00'),
('RE022', 'GP05', 'TD03', 'TB001', 3, 'CONFIRMED', '2024-05-20 09:15:00'),
('RE023', 'GP05', 'TD05', 'DR001', 5, 'CONFIRMED', '2024-05-20 09:30:00'),
('RE024', 'GP05', 'TDJS', 'TA001', 7, 'CONFIRMED', '2024-05-20 09:45:00'),
('RE025', 'GP05', 'TDMD', 'FW001', 9, 'CONFIRMED', '2024-05-20 10:00:00'),
('RE026', 'GP05', 'TDNVA', 'SW001', 11, 'CONFIRMED', '2024-05-20 10:15:00'),
('RE027', 'GP05', 'TDHM', 'RF001', 13, 'CONFIRMED', '2024-05-20 10:30:00'),
('RE028', 'GP05', 'TDLT', 'BS001', 18, 'CONFIRMED', '2024-05-20 10:45:00'),
('RE029', 'GP05', 'TDAK', 'RF001', 20, 'CONFIRMED', '2024-05-20 11:00:00'),
('RE030', 'GP05', 'TDJT', 'ES001', 23, 'CONFIRMED', '2024-05-20 11:15:00'),

-- 2023 Season entries
-- Monaco Grand Prix 2023
('RE031', 'GP10', 'TD01', 'RF001', 1, 'CONFIRMED', '2023-05-21 09:00:00'),
('RE032', 'GP10', 'TD02', 'TA001', 2, 'CONFIRMED', '2023-05-21 09:15:00'),
('RE033', 'GP10', 'TD03', 'SR001', 3, 'CONFIRMED', '2023-05-21 09:30:00'),
('RE034', 'GP10', 'TD04', 'FW001', 4, 'CONFIRMED', '2023-05-21 09:45:00'),
('RE035', 'GP10', 'TD05', 'SR002', 5, 'CONFIRMED', '2023-05-21 10:00:00'),
('RE036', 'GP10', 'TD06', 'SW001', 6, 'CONFIRMED', '2023-05-21 10:15:00'),
('RE037', 'GP10', 'TDJS', 'BS001', 7, 'CONFIRMED', '2023-05-21 10:30:00'),
('RE038', 'GP10', 'TDCG', 'GD001', 8, 'CONFIRMED', '2023-05-21 10:45:00'),

-- British Grand Prix 2023
('RE039', 'GP11', 'TD01', 'RF001', 1, 'CONFIRMED', '2023-07-10 09:00:00'),
('RE040', 'GP11', 'TD02', 'TA001', 2, 'CONFIRMED', '2023-07-10 09:15:00'),
('RE041', 'GP11', 'TD03', 'SR001', 3, 'CONFIRMED', '2023-07-10 09:30:00'),
('RE042', 'GP11', 'TD04', 'FW001', 4, 'CONFIRMED', '2023-07-10 09:45:00'),
('RE043', 'GP11', 'TD05', 'SR002', 5, 'CONFIRMED', '2023-07-10 10:00:00'),
('RE044', 'GP11', 'TD06', 'SW001', 6, 'CONFIRMED', '2023-07-10 10:15:00'),
('RE045', 'GP11', 'TDJS', 'BS001', 7, 'CONFIRMED', '2023-07-10 10:30:00'),

-- 2022 Season entries
-- Italian Grand Prix 2022
('RE046', 'GP17', 'TDHM', 'SW001', 13, 'CONFIRMED', '2022-09-04 09:00:00'),
('RE047', 'GP17', 'TDSC', 'TB001', 16, 'CONFIRMED', '2022-09-04 09:15:00'),
('RE048', 'GP17', 'TDAR', 'FW001', 12, 'CONFIRMED', '2022-09-04 09:30:00'),
('RE049', 'GP17', 'TDJP', 'PF001', 14, 'CONFIRMED', '2022-09-04 09:45:00'),
('RE050', 'GP17', 'TDLT', 'DR001', 18, 'CONFIRMED', '2022-09-04 10:00:00');

-- Insert sample race results for completed races
-- Italian Grand Prix 2024 results (GP01)
INSERT INTO driver_race_results (id, race_stage_id, driver_id, team_id, season_id, status, laps_completed) VALUES
('RR001', 'GP01', 'TD01', 'DR001', '2024', 'PENDING', 0),
('RR002', 'GP01', 'TD02', 'TB001', '2024', 'PENDING', 0),
('RR003', 'GP01', 'TD03', 'TB001', '2024', 'PENDING', 0),
('RR004', 'GP01', 'TD04', 'PF001', '2024', 'PENDING', 0),
('RR005', 'GP01', 'TD05', 'DR001', '2024', 'PENDING', 0),
('RR006', 'GP01', 'TD06', 'PF001', '2024', 'PENDING', 0),
('RR007', 'GP01', 'TDJS', 'TA001', '2024', 'PENDING', 0),
('RR008', 'GP01', 'TDCG', 'SR001', '2024', 'PENDING', 0),
('RR009', 'GP01', 'TDMD', 'FW001', '2024', 'PENDING', 0),
('RR010', 'GP01', 'TDTY', 'SR002', '2024', 'PENDING', 0),

-- Spanish Grand Prix 2024 results (GP03)
('RR011', 'GP03', 'TDJS', 'TA001', '2024', 'PENDING', 0),
('RR012', 'GP03', 'TDCG', 'SR001', '2024', 'PENDING', 0),
('RR013', 'GP03', 'TDMD', 'FW001', '2024', 'PENDING', 0),
('RR014', 'GP03', 'TDTY', 'SR002', '2024', 'PENDING', 0),
('RR015', 'GP03', 'TDNVA', 'SW001', '2024', 'PENDING', 0),
('RR016', 'GP03', 'TDAR', 'SR001', '2024', 'PENDING', 0),
('RR017', 'GP03', 'TDJP', 'TA001', '2024', 'PENDING', 0),
('RR018', 'GP03', 'TDRL', 'LP001', '2024', 'PENDING', 0),
('RR019', 'GP03', 'TDSC', 'GD001', '2024', 'PENDING', 0),
('RR020', 'GP03', 'TDPK', 'TB001', '2024', 'PENDING', 0),

-- Monaco Grand Prix 2024 results (GP05)
('RR021', 'GP05', 'TD01', 'DR001', '2024', 'PENDING', 0),
('RR022', 'GP05', 'TD03', 'TB001', '2024', 'PENDING', 0),
('RR023', 'GP05', 'TD05', 'DR001', '2024', 'PENDING', 0),
('RR024', 'GP05', 'TDJS', 'TA001', '2024', 'PENDING', 0),
('RR025', 'GP05', 'TDMD', 'FW001', '2024', 'PENDING', 0),
('RR026', 'GP05', 'TDNVA', 'SW001', '2024', 'PENDING', 0),
('RR027', 'GP05', 'TDHM', 'RF001', '2024', 'PENDING', 0),
('RR028', 'GP05', 'TDLT', 'BS001', '2024', 'PENDING', 0),
('RR029', 'GP05', 'TDAK', 'RF001', '2024', 'PENDING', 0),
('RR030', 'GP05', 'TDJT', 'ES001', '2024', 'PENDING', 0),

-- Monaco Grand Prix 2023 results (GP10)
('RR031', 'GP10', 'TD01', 'RF001', '2023', 'PENDING', 0),
('RR032', 'GP10', 'TD02', 'TA001', '2023', 'PENDING', 0),
('RR033', 'GP10', 'TD03', 'SR001', '2023', 'PENDING', 0),
('RR034', 'GP10', 'TD04', 'FW001', '2023', 'PENDING', 0),
('RR035', 'GP10', 'TD05', 'SR002', '2023', 'PENDING', 0),
('RR036', 'GP10', 'TD06', 'SW001', '2023', 'PENDING', 0),
('RR037', 'GP10', 'TDJS', 'BS001', '2023', 'PENDING', 0),
('RR038', 'GP10', 'TDCG', 'GD001', '2023', 'PENDING', 0),

-- British Grand Prix 2023 results (GP11)
('RR039', 'GP11', 'TD01', 'RF001', '2023', 'PENDING', 0),
('RR040', 'GP11', 'TD02', 'TA001', '2023', 'PENDING', 0),
('RR041', 'GP11', 'TD03', 'SR001', '2023', 'PENDING', 0),
('RR042', 'GP11', 'TD04', 'FW001', '2023', 'PENDING', 0),
('RR043', 'GP11', 'TD05', 'SR002', '2023', 'PENDING', 0),
('RR044', 'GP11', 'TD06', 'SW001', '2023', 'PENDING', 0),
('RR045', 'GP11', 'TDJS', 'BS001', '2023', 'PENDING', 0),

-- Italian Grand Prix 2022 results (GP17)
('RR046', 'GP17', 'TDHM', 'SW001', '2022', 'PENDING', 0),
('RR047', 'GP17', 'TDSC', 'TB001', '2022', 'PENDING', 0),
('RR048', 'GP17', 'TDAR', 'FW001', '2022', 'PENDING', 0),
('RR049', 'GP17', 'TDJP', 'PF001', '2022', 'PENDING', 0),
('RR050', 'GP17', 'TDLT', 'DR001', '2022', 'PENDING', 0); 