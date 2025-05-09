CREATE DATABASE IF NOT EXISTS race_result_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE race_result_db;

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- Create race_entries table
CREATE TABLE IF NOT EXISTS race_entries (
  id VARCHAR(36) PRIMARY KEY,
  race_stage_id VARCHAR(36) NOT NULL,
  driver_id VARCHAR(36) NOT NULL,
  team_id VARCHAR(36) NOT NULL,
  car_number INT,
  entry_status VARCHAR(20) NOT NULL,
  registered_at DATETIME,
  UNIQUE KEY unique_race_driver (race_stage_id, driver_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create driver_race_results table
CREATE TABLE IF NOT EXISTS driver_race_results (
  id VARCHAR(36) PRIMARY KEY,
  race_stage_id VARCHAR(36) NOT NULL,
  driver_id VARCHAR(36) NOT NULL,
  team_id VARCHAR(36) NOT NULL,
  driver_name VARCHAR(255),
  team_name VARCHAR(255),
  season_id VARCHAR(36) NOT NULL,
  grid_position INT,
  finish_position INT,
  points INT,
  status VARCHAR(20) NOT NULL DEFAULT 'FINISHED',
  finish_time_or_gap VARCHAR(100),
  laps_completed INT,
  UNIQUE KEY unique_race_driver_result (race_stage_id, driver_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create seasons_ref table (for reference in queries)
CREATE TABLE IF NOT EXISTS seasons_ref (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert reference data for seasons
INSERT INTO seasons_ref (id, name) VALUES
('2024', 'Formula 1 2024 Season'),
('2023', 'Formula 1 2023 Season'),
('2022', 'Formula 1 2022 Season');

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
INSERT INTO driver_race_results (id, race_stage_id, driver_id, driver_name, team_id, team_name, season_id, status) VALUES
('RR001', 'GP01', 'TD01', 'Nguyễn Văn An', 'DR001', 'Dragon Racing', '2024', 'PENDING'),
('RR002', 'GP01', 'TD02', 'Lê Thị Hồng', 'TB001', 'Thunder Bolts', '2024', 'PENDING'),
('RR003', 'GP01', 'TD03', 'Bùi Trí Công', 'TB001', 'Thunder Bolts', '2024', 'PENDING'),
('RR004', 'GP01', 'TD04', 'Trần Văn Hoàng', 'PF001', 'Phoenix Flyers', '2024', 'PENDING'),
('RR005', 'GP01', 'TD05', 'Lê Văn Hữu', 'DR001', 'Dragon Racing', '2024', 'PENDING'),
('RR006', 'GP01', 'TD06', 'Mai Văn Sơn', 'PF001', 'Phoenix Flyers', '2024', 'PENDING'),
('RR007', 'GP01', 'TDJS', 'John Smith', 'TA001', 'Team Alpha', '2024', 'PENDING'),
('RR008', 'GP01', 'TDCG', 'Carlos Garcia', 'SR001', 'Speed Racers', '2024', 'PENDING'),
('RR009', 'GP01', 'TDMD', 'Michel Dubois', 'FW001', 'Fast Wheels', '2024', 'PENDING'),
('RR010', 'GP01', 'TDTY', 'Takeshi Yamada', 'SR002', 'Samurai Racing', '2024', 'PENDING'),

-- Spanish Grand Prix 2024 results (GP03)
('RR011', 'GP03', 'TDJS', 'John Smith', 'TA001', 'Team Alpha', '2024', 'PENDING'),
('RR012', 'GP03', 'TDCG', 'Carlos Garcia', 'SR001', 'Speed Racers', '2024', 'PENDING'),
('RR013', 'GP03', 'TDMD', 'Michel Dubois', 'FW001', 'Fast Wheels', '2024', 'PENDING'),
('RR014', 'GP03', 'TDTY', 'Takeshi Yamada', 'SR002', 'Samurai Racing', '2024', 'PENDING'),
('RR015', 'GP03', 'TDNVA', 'Nguyễn Văn A', 'SW001', 'Speed Warriors', '2024', 'PENDING'),
('RR016', 'GP03', 'TDAR', 'Antonio Rodriguez', 'SR001', 'Speed Racers', '2024', 'PENDING'),
('RR017', 'GP03', 'TDJP', 'James Parker', 'TA001', 'Team Alpha', '2024', 'PENDING'),
('RR018', 'GP03', 'TDRL', 'Ricardo Lopez', 'LP001', 'Lightning Panthers', '2024', 'PENDING'),
('RR019', 'GP03', 'TDSC', 'Sarah Chen', 'GD001', 'Golden Dragons', '2024', 'PENDING'),
('RR020', 'GP03', 'TDPK', 'Pavel Kowalski', 'TB001', 'Thunder Bolts', '2024', 'PENDING'),

-- Monaco Grand Prix 2024 results (GP05)
('RR021', 'GP05', 'TD01', 'Nguyễn Văn An', 'DR001', 'Dragon Racing', '2024', 'PENDING'),
('RR022', 'GP05', 'TD03', 'Bùi Trí Công', 'TB001', 'Thunder Bolts', '2024', 'PENDING'),
('RR023', 'GP05', 'TD05', 'Lê Văn Hữu', 'DR001', 'Dragon Racing', '2024', 'PENDING'),
('RR024', 'GP05', 'TDJS', 'John Smith', 'TA001', 'Team Alpha', '2024', 'PENDING'),
('RR025', 'GP05', 'TDMD', 'Michel Dubois', 'FW001', 'Fast Wheels', '2024', 'PENDING'),
('RR026', 'GP05', 'TDNVA', 'Nguyễn Văn A', 'SW001', 'Speed Warriors', '2024', 'PENDING'),
('RR027', 'GP05', 'TDHM', 'Hans Mueller', 'RF001', 'Red Foxes', '2024', 'PENDING'),
('RR028', 'GP05', 'TDLT', 'Laura Thompson', 'BS001', 'Blue Stars', '2024', 'PENDING'),
('RR029', 'GP05', 'TDAK', 'Alexei Kozlov', 'RF001', 'Red Foxes', '2024', 'PENDING'),
('RR030', 'GP05', 'TDJT', 'Jack Thompson', 'ES001', 'Eagle Squadron', '2024', 'PENDING'),

-- Monaco Grand Prix 2023 results (GP10)
('RR031', 'GP10', 'TD01', 'Nguyễn Văn An', 'RF001', 'Red Foxes', '2023', 'PENDING'),
('RR032', 'GP10', 'TD02', 'Lê Thị Hồng', 'TA001', 'Team Alpha', '2023', 'PENDING'),
('RR033', 'GP10', 'TD03', 'Bùi Trí Công', 'SR001', 'Speed Racers', '2023', 'PENDING'),
('RR034', 'GP10', 'TD04', 'Trần Văn Hoàng', 'FW001', 'Fast Wheels', '2023', 'PENDING'),
('RR035', 'GP10', 'TD05', 'Lê Văn Hữu', 'SR002', 'Samurai Racing', '2023', 'PENDING'),
('RR036', 'GP10', 'TD06', 'Mai Văn Sơn', 'SW001', 'Speed Warriors', '2023', 'PENDING'),
('RR037', 'GP10', 'TDJS', 'John Smith', 'BS001', 'Blue Stars', '2023', 'PENDING'),
('RR038', 'GP10', 'TDCG', 'Carlos Garcia', 'GD001', 'Golden Dragons', '2023', 'PENDING'),

-- British Grand Prix 2023 results (GP11)
('RR039', 'GP11', 'TD01', 'Nguyễn Văn An', 'RF001', 'Red Foxes', '2023', 'PENDING'),
('RR040', 'GP11', 'TD02', 'Lê Thị Hồng', 'TA001', 'Team Alpha', '2023', 'PENDING'),
('RR041', 'GP11', 'TD03', 'Bùi Trí Công', 'SR001', 'Speed Racers', '2023', 'PENDING'),
('RR042', 'GP11', 'TD04', 'Trần Văn Hoàng', 'FW001', 'Fast Wheels', '2023', 'PENDING'),
('RR043', 'GP11', 'TD05', 'Lê Văn Hữu', 'SR002', 'Samurai Racing', '2023', 'PENDING'),
('RR044', 'GP11', 'TD06', 'Mai Văn Sơn', 'SW001', 'Speed Warriors', '2023', 'PENDING'),
('RR045', 'GP11', 'TDJS', 'John Smith', 'BS001', 'Blue Stars', '2023', 'PENDING'),

-- Italian Grand Prix 2022 results (GP17)
('RR046', 'GP17', 'TDHM', 'Hans Mueller', 'SW001', 'Speed Warriors', '2022', 'PENDING'),
('RR047', 'GP17', 'TDSC', 'Sarah Chen', 'TB001', 'Thunder Bolts', '2022', 'PENDING'),
('RR048', 'GP17', 'TDAR', 'Antonio Rodriguez', 'FW001', 'Fast Wheels', '2022', 'PENDING'),
('RR049', 'GP17', 'TDJP', 'James Parker', 'PF001', 'Phoenix Flyers', '2022', 'PENDING'),
('RR050', 'GP17', 'TDLT', 'Laura Thompson', 'DR001', 'Dragon Racing', '2022', 'PENDING');
