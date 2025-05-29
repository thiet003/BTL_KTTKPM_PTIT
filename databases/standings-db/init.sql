CREATE DATABASE IF NOT EXISTS standings_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE standings_db;

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- Create seasons table
CREATE TABLE IF NOT EXISTS seasons (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  year INT NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create drivers table
CREATE TABLE IF NOT EXISTS drivers (
  id VARCHAR(36) PRIMARY KEY,
  full_name VARCHAR(255) NOT NULL,
  nationality VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create teams table
CREATE TABLE IF NOT EXISTS teams (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  country VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create driver_team_assignments table
CREATE TABLE IF NOT EXISTS driver_team_assignments (
  id VARCHAR(36) PRIMARY KEY,
  driver_id VARCHAR(36) NOT NULL,
  team_id VARCHAR(36) NOT NULL,
  start_date DATE,
  end_date DATE,
  FOREIGN KEY (driver_id) REFERENCES drivers(id),
  FOREIGN KEY (team_id) REFERENCES teams(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create driver_standings table with foreign keys (removed redundant fields)
CREATE TABLE IF NOT EXISTS driver_standings (
  id VARCHAR(36) PRIMARY KEY,
  season_id VARCHAR(36) NOT NULL,
  driver_id VARCHAR(36) NOT NULL,
  last_team_id VARCHAR(36),
  total_points INT NOT NULL DEFAULT 0,
  `rank` INT,
  wins INT NOT NULL DEFAULT 0,
  podiums INT NOT NULL DEFAULT 0,
  last_calculated DATETIME,
  UNIQUE KEY unique_season_driver (season_id, driver_id),
  FOREIGN KEY (season_id) REFERENCES seasons(id),
  FOREIGN KEY (driver_id) REFERENCES drivers(id),
  FOREIGN KEY (last_team_id) REFERENCES teams(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert seasons data
INSERT INTO seasons (id, name, year) VALUES
('2022', 'Formula 1 2022 Season', 2022),
('2023', 'Formula 1 2023 Season', 2023),
('2024', 'Formula 1 2024 Season', 2024);

-- Insert drivers data
INSERT INTO drivers (id, full_name, nationality) VALUES
('TD01', 'Nguyễn Văn An', 'Vietnam'),
('TD02', 'Lê Thị Hồng', 'Vietnam'),
('TD03', 'Bùi Trí Công', 'Vietnam'),
('TD04', 'Trần Văn Hoàng', 'Vietnam'),
('TD05', 'Lê Văn Hữu', 'Vietnam'),
('TD06', 'Mai Văn Sơn', 'Vietnam'),
('TDNVA', 'Nguyễn Văn A', 'Vietnam'),
('TDJS', 'John Smith', 'UK'),
('TDCG', 'Carlos Garcia', 'Spain'),
('TDMD', 'Michel Dubois', 'France'),
('TDTY', 'Takeshi Yamada', 'Japan'),
('TDAR', 'Antonio Rodriguez', 'Spain'),
('TDJP', 'James Parker', 'UK'),
('TDRL', 'Ricardo Lopez', 'Brazil'),
('TDSC', 'Sarah Chen', 'China'),
('TDPK', 'Pavel Kowalski', 'Poland'),
('TDHM', 'Hans Mueller', 'Germany'),
('TDLT', 'Laura Thompson', 'Australia'),
('TDAK', 'Alexei Kozlov', 'Russia'),
('TDJT', 'Jack Thompson', 'USA'),
('TDMK', 'Maria Kim', 'South Korea'),
('TDMS', 'Mohamed Salah', 'Egypt');

-- Insert teams data
INSERT INTO teams (id, name, country) VALUES
('SW001', 'Speed Warriors', 'Italy'),
('TA001', 'Team Alpha', 'UK'),
('SR001', 'Speed Racers', 'Spain'),
('FW001', 'Fast Wheels', 'France'),
('SR002', 'Samurai Racing', 'Japan'),
('TB001', 'Thunder Bolts', 'Germany'),
('PF001', 'Phoenix Flyers', 'USA'),
('DR001', 'Dragon Racing', 'China'),
('LP001', 'Lightning Panthers', 'Brazil'),
('GD001', 'Golden Dragons', 'China'),
('RF001', 'Red Foxes', 'Germany'),
('BS001', 'Blue Stars', 'Australia'),
('ES001', 'Eagle Squadron', 'USA'),
('SC001', 'Silver Comets', 'France');

-- Insert driver team assignments data
INSERT INTO driver_team_assignments (id, driver_id, team_id, start_date, end_date) VALUES
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
('DTA036', 'TDLT', 'DR001', '2022-01-01', '2022-12-31'),
('DTA037', 'TDPK', 'ES001', '2022-01-01', '2022-12-31'),
('DTA038', 'TDMK', 'SC001', '2022-01-01', '2022-12-31'),
('DTA039', 'TDRL', 'GD001', '2022-01-01', '2022-12-31'),
('DTA040', 'TDAK', 'BS001', '2022-01-01', '2022-12-31'),
('DTA041', 'TDMS', 'LP001', '2022-01-01', '2022-12-31');

-- Insert sample driver standings data (without redundant fields)
-- 2024 Season Standings
INSERT INTO driver_standings (id, season_id, driver_id, last_team_id, total_points, `rank`, wins, podiums, last_calculated) VALUES
('DS001', '2024', 'TDNVA', 'SW001', 0, NULL, 0, 0, NULL),
('DS002', '2024', 'TDJS', 'TA001', 0, NULL, 0, 0, NULL),
('DS003', '2024', 'TDCG', 'SR001', 0, NULL, 0, 0, NULL),
('DS004', '2024', 'TDMD', 'FW001', 0, NULL, 0, 0, NULL),
('DS005', '2024', 'TDTY', 'SR002', 0, NULL, 0, 0, NULL),
('DS006', '2024', 'TD03', 'TB001', 0, NULL, 0, 0, NULL),
('DS007', '2024', 'TD06', 'PF001', 0, NULL, 0, 0, NULL),
('DS008', '2024', 'TD01', 'DR001', 0, NULL, 0, 0, NULL),
('DS009', '2024', 'TD05', 'DR001', 0, NULL, 0, 0, NULL),
('DS010', '2024', 'TD02', 'TB001', 0, NULL, 0, 0, NULL),
('DS011', '2024', 'TD04', 'PF001', 0, NULL, 0, 0, NULL),
('DS012', '2024', 'TDAR', 'SR001', 0, NULL, 0, 0, NULL),
('DS013', '2024', 'TDJP', 'TA001', 0, NULL, 0, 0, NULL),
('DS014', '2024', 'TDRL', 'LP001', 0, NULL, 0, 0, NULL),
('DS015', '2024', 'TDSC', 'GD001', 0, NULL, 0, 0, NULL),
('DS016', '2024', 'TDPK', 'TB001', 0, NULL, 0, 0, NULL),
('DS017', '2024', 'TDHM', 'RF001', 0, NULL, 0, 0, NULL),
('DS018', '2024', 'TDLT', 'BS001', 0, NULL, 0, 0, NULL),
('DS019', '2024', 'TDAK', 'RF001', 0, NULL, 0, 0, NULL),
('DS020', '2024', 'TDJT', 'ES001', 0, NULL, 0, 0, NULL),

-- 2023 Season Standings
('DS021', '2023', 'TD01', 'RF001', 0, NULL, 0, 0, NULL),
('DS022', '2023', 'TD02', 'TA001', 0, NULL, 0, 0, NULL),
('DS023', '2023', 'TD03', 'SR001', 0, NULL, 0, 0, NULL),
('DS024', '2023', 'TD04', 'FW001', 0, NULL, 0, 0, NULL),
('DS025', '2023', 'TD05', 'SR002', 0, NULL, 0, 0, NULL),
('DS026', '2023', 'TD06', 'SW001', 0, NULL, 0, 0, NULL),
('DS027', '2023', 'TDJS', 'BS001', 0, NULL, 0, 0, NULL),
('DS028', '2023', 'TDCG', 'GD001', 0, NULL, 0, 0, NULL),
('DS029', '2023', 'TDMD', 'SC001', 0, NULL, 0, 0, NULL),
('DS030', '2023', 'TDTY', 'ES001', 0, NULL, 0, 0, NULL),
('DS031', '2023', 'TDNVA', 'LP001', 0, NULL, 0, 0, NULL),

-- 2022 Season Standings
('DS032', '2022', 'TDHM', 'SW001', 0, NULL, 0, 0, NULL),
('DS033', '2022', 'TDSC', 'TB001', 0, NULL, 0, 0, NULL),
('DS034', '2022', 'TDAR', 'FW001', 0, NULL, 0, 0, NULL),
('DS035', '2022', 'TDJP', 'PF001', 0, NULL, 0, 0, NULL),
('DS036', '2022', 'TDLT', 'DR001', 0, NULL, 0, 0, NULL),
('DS037', '2022', 'TDPK', 'ES001', 0, NULL, 0, 0, NULL),
('DS038', '2022', 'TDMK', 'SC001', 0, NULL, 0, 0, NULL),
('DS039', '2022', 'TDRL', 'GD001', 0, NULL, 0, 0, NULL),
('DS040', '2022', 'TDAK', 'BS001', 0, NULL, 0, 0, NULL),
('DS041', '2022', 'TDMS', 'LP001', 0, NULL, 0, 0, NULL);

