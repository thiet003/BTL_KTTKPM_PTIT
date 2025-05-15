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

-- Create driver_standings table with foreign keys
CREATE TABLE IF NOT EXISTS driver_standings (
  id VARCHAR(36) PRIMARY KEY,
  season_id VARCHAR(36) NOT NULL,
  driver_id VARCHAR(36) NOT NULL,
  team_id VARCHAR(36),
  driver_name VARCHAR(255) NOT NULL,
  team_name VARCHAR(255),
  nationality VARCHAR(100),
  total_points INT NOT NULL DEFAULT 0,
  `rank` INT,
  wins INT NOT NULL DEFAULT 0,
  podiums INT NOT NULL DEFAULT 0,
  last_calculated DATETIME,
  UNIQUE KEY unique_season_driver (season_id, driver_id),
  FOREIGN KEY (season_id) REFERENCES seasons(id),
  FOREIGN KEY (driver_id) REFERENCES drivers(id),
  FOREIGN KEY (team_id) REFERENCES teams(id)
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

-- Insert sample driver standings data with minimal information (no points or rankings calculated yet)
-- 2024 Season Standings
INSERT INTO driver_standings (id, season_id, driver_id, team_id, driver_name, team_name, nationality, total_points, `rank`, wins, podiums, last_calculated) VALUES
('DS001', '2024', 'TDNVA', 'SW001', 'Nguyễn Văn A', 'Speed Warriors', 'Vietnam', 0, NULL, 0, 0, NULL),
('DS002', '2024', 'TDJS', 'TA001', 'John Smith', 'Team Alpha', 'UK', 0, NULL, 0, 0, NULL),
('DS003', '2024', 'TDCG', 'SR001', 'Carlos Garcia', 'Speed Racers', 'Spain', 0, NULL, 0, 0, NULL),
('DS004', '2024', 'TDMD', 'FW001', 'Michel Dubois', 'Fast Wheels', 'France', 0, NULL, 0, 0, NULL),
('DS005', '2024', 'TDTY', 'SR002', 'Takeshi Yamada', 'Samurai Racing', 'Japan', 0, NULL, 0, 0, NULL),
('DS006', '2024', 'TD03', 'TB001', 'Bùi Trí Công', 'Thunder Bolts', 'Vietnam', 0, NULL, 0, 0, NULL),
('DS007', '2024', 'TD06', 'PF001', 'Mai Văn Sơn', 'Phoenix Flyers', 'Vietnam', 0, NULL, 0, 0, NULL),
('DS008', '2024', 'TD01', 'DR001', 'Nguyễn Văn An', 'Dragon Racing', 'Vietnam', 0, NULL, 0, 0, NULL),
('DS009', '2024', 'TD05', 'DR001', 'Lê Văn Hữu', 'Dragon Racing', 'Vietnam', 0, NULL, 0, 0, NULL),
('DS010', '2024', 'TD02', 'TB001', 'Lê Thị Hồng', 'Thunder Bolts', 'Vietnam', 0, NULL, 0, 0, NULL),
('DS011', '2024', 'TD04', 'PF001', 'Trần Văn Hoàng', 'Phoenix Flyers', 'Vietnam', 0, NULL, 0, 0, NULL),
('DS012', '2024', 'TDAR', 'SR001', 'Antonio Rodriguez', 'Speed Racers', 'Spain', 0, NULL, 0, 0, NULL),
('DS013', '2024', 'TDJP', 'TA001', 'James Parker', 'Team Alpha', 'UK', 0, NULL, 0, 0, NULL),
('DS014', '2024', 'TDRL', 'LP001', 'Ricardo Lopez', 'Lightning Panthers', 'Brazil', 0, NULL, 0, 0, NULL),
('DS015', '2024', 'TDSC', 'GD001', 'Sarah Chen', 'Golden Dragons', 'China', 0, NULL, 0, 0, NULL),
('DS016', '2024', 'TDPK', 'TB001', 'Pavel Kowalski', 'Thunder Bolts', 'Poland', 0, NULL, 0, 0, NULL),
('DS017', '2024', 'TDHM', 'RF001', 'Hans Mueller', 'Red Foxes', 'Germany', 0, NULL, 0, 0, NULL),
('DS018', '2024', 'TDLT', 'BS001', 'Laura Thompson', 'Blue Stars', 'Australia', 0, NULL, 0, 0, NULL),
('DS019', '2024', 'TDAK', 'RF001', 'Alexei Kozlov', 'Red Foxes', 'Russia', 0, NULL, 0, 0, NULL),
('DS020', '2024', 'TDJT', 'ES001', 'Jack Thompson', 'Eagle Squadron', 'USA', 0, NULL, 0, 0, NULL),

-- 2023 Season Standings
('DS021', '2023', 'TD01', 'RF001', 'Nguyễn Văn An', 'Red Foxes', 'Vietnam', 0, NULL, 0, 0, NULL),
('DS022', '2023', 'TD02', 'TA001', 'Lê Thị Hồng', 'Team Alpha', 'Vietnam', 0, NULL, 0, 0, NULL),
('DS023', '2023', 'TD03', 'SR001', 'Bùi Trí Công', 'Speed Racers', 'Vietnam', 0, NULL, 0, 0, NULL),
('DS024', '2023', 'TD04', 'FW001', 'Trần Văn Hoàng', 'Fast Wheels', 'Vietnam', 0, NULL, 0, 0, NULL),
('DS025', '2023', 'TD05', 'SR002', 'Lê Văn Hữu', 'Samurai Racing', 'Vietnam', 0, NULL, 0, 0, NULL),
('DS026', '2023', 'TD06', 'SW001', 'Mai Văn Sơn', 'Speed Warriors', 'Vietnam', 0, NULL, 0, 0, NULL),
('DS027', '2023', 'TDJS', 'BS001', 'John Smith', 'Blue Stars', 'UK', 0, NULL, 0, 0, NULL),
('DS028', '2023', 'TDCG', 'GD001', 'Carlos Garcia', 'Golden Dragons', 'Spain', 0, NULL, 0, 0, NULL),
('DS029', '2023', 'TDMD', 'SC001', 'Michel Dubois', 'Silver Comets', 'France', 0, NULL, 0, 0, NULL),
('DS030', '2023', 'TDTY', 'ES001', 'Takeshi Yamada', 'Eagle Squadron', 'Japan', 0, NULL, 0, 0, NULL),
('DS031', '2023', 'TDNVA', 'LP001', 'Nguyễn Văn A', 'Lightning Panthers', 'Vietnam', 0, NULL, 0, 0, NULL),

-- 2022 Season Standings
('DS032', '2022', 'TDHM', 'SW001', 'Hans Mueller', 'Speed Warriors', 'Germany', 0, NULL, 0, 0, NULL),
('DS033', '2022', 'TDSC', 'TB001', 'Sarah Chen', 'Thunder Bolts', 'China', 0, NULL, 0, 0, NULL),
('DS034', '2022', 'TDAR', 'FW001', 'Antonio Rodriguez', 'Fast Wheels', 'Spain', 0, NULL, 0, 0, NULL),
('DS035', '2022', 'TDJP', 'PF001', 'James Parker', 'Phoenix Flyers', 'UK', 0, NULL, 0, 0, NULL),
('DS036', '2022', 'TDLT', 'DR001', 'Laura Thompson', 'Dragon Racing', 'Australia', 0, NULL, 0, 0, NULL),
('DS037', '2022', 'TDPK', 'ES001', 'Pavel Kowalski', 'Eagle Squadron', 'Poland', 0, NULL, 0, 0, NULL),
('DS038', '2022', 'TDMK', 'SC001', 'Maria Kim', 'Silver Comets', 'South Korea', 0, NULL, 0, 0, NULL),
('DS039', '2022', 'TDRL', 'GD001', 'Ricardo Lopez', 'Golden Dragons', 'Brazil', 0, NULL, 0, 0, NULL),
('DS040', '2022', 'TDAK', 'BS001', 'Alexei Kozlov', 'Blue Stars', 'Russia', 0, NULL, 0, 0, NULL),
('DS041', '2022', 'TDMS', 'LP001', 'Mohamed Salah', 'Lightning Panthers', 'Egypt', 0, NULL, 0, 0, NULL);

