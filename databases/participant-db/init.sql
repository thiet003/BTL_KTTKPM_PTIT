CREATE DATABASE IF NOT EXISTS participant_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE participant_db;

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- Create racing_teams table
CREATE TABLE IF NOT EXISTS racing_teams (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  base_location VARCHAR(255),
  manufacturer VARCHAR(100),
  country VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create drivers table
CREATE TABLE IF NOT EXISTS drivers (
  id VARCHAR(36) PRIMARY KEY,
  full_name VARCHAR(255) NOT NULL,
  nationality VARCHAR(100) NOT NULL,
  date_of_birth DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create driver_team_assignments table
CREATE TABLE IF NOT EXISTS driver_team_assignments (
  id VARCHAR(36) PRIMARY KEY,
  driver_id VARCHAR(36) NOT NULL,
  team_id VARCHAR(36) NOT NULL,
  start_date DATE NOT NULL,
  end_date DATE,
  FOREIGN KEY (driver_id) REFERENCES drivers(id),
  FOREIGN KEY (team_id) REFERENCES racing_teams(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample data for racing_teams
INSERT INTO racing_teams (id, name, base_location, manufacturer, country) VALUES
('DR001', 'Dragon Racing', 'London, UK', 'Dragon Motors', 'UK'),
('TB001', 'Thunder Bolts', 'Milan, Italy', 'Thunder Technologies', 'Italy'),
('PF001', 'Phoenix Flyers', 'Barcelona, Spain', 'Phoenix Cars', 'Spain'),
('TA001', 'Team Alpha', 'Oxford, UK', 'Alpha Engineering', 'UK'),
('SR001', 'Speed Racers', 'Valencia, Spain', 'Speed Motors', 'Spain'),
('SW001', 'Speed Warriors', 'Hanoi, Vietnam', 'Warrior Tech', 'Vietnam'),
('FW001', 'Fast Wheels', 'Paris, France', 'Fast Engineering', 'France'),
('SR002', 'Samurai Racing', 'Tokyo, Japan', 'Samurai Tech', 'Japan'),
('RF001', 'Red Foxes', 'Berlin, Germany', 'Fox Automotive', 'Germany'),
('BS001', 'Blue Stars', 'Melbourne, Australia', 'Star Racing', 'Australia'),
('SC001', 'Silver Comets', 'Montreal, Canada', 'Comet Tech', 'Canada'),
('GD001', 'Golden Dragons', 'Shanghai, China', 'Dragon Technology', 'China'),
('ES001', 'Eagle Squadron', 'New York, USA', 'Eagle Motors', 'USA'),
('LP001', 'Lightning Panthers', 'Sao Paulo, Brazil', 'Panther Racing', 'Brazil');

-- Insert sample data for drivers
INSERT INTO drivers (id, full_name, nationality, date_of_birth) VALUES
('TD01', 'Nguyễn Văn An', 'Vietnam', '1990-03-15'),
('TD02', 'Lê Thị Hồng', 'Vietnam', '1992-07-22'),
('TD03', 'Bùi Trí Công', 'Vietnam', '1988-11-05'),
('TD04', 'Trần Văn Hoàng', 'Vietnam', '1995-02-18'),
('TD05', 'Lê Văn Hữu', 'Vietnam', '1991-09-30'),
('TD06', 'Mai Văn Sơn', 'Vietnam', '1993-12-25'),
('TDJS', 'John Smith', 'UK', '1990-01-10'),
('TDCG', 'Carlos Garcia', 'Spain', '1992-05-15'),
('TDMD', 'Michel Dubois', 'France', '1988-08-20'),
('TDTY', 'Takeshi Yamada', 'Japan', '1991-11-05'),
('TDNVA', 'Nguyễn Văn A', 'Vietnam', '1989-04-12'),
('TDHM', 'Hans Mueller', 'Germany', '1994-03-22'),
('TDSC', 'Sarah Chen', 'China', '1993-09-18'),
('TDAR', 'Antonio Rodriguez', 'Spain', '1990-12-05'),
('TDJP', 'James Parker', 'UK', '1989-05-29'),
('TDLT', 'Laura Thompson', 'Australia', '1991-07-15'),
('TDPK', 'Pavel Kowalski', 'Poland', '1992-11-30'),
('TDMK', 'Maria Kim', 'South Korea', '1995-01-08'),
('TDRL', 'Ricardo Lopez', 'Brazil', '1990-08-12'),
('TDAK', 'Alexei Kozlov', 'Russia', '1988-06-23'),
('TDMS', 'Mohamed Salah', 'Egypt', '1994-04-16'),
('TDNO', 'Nina Olsen', 'Sweden', '1993-10-02'),
('TDJT', 'Jack Thompson', 'USA', '1992-02-19'),
('TDEM', 'Emma Martinez', 'Canada', '1994-09-07');

-- Insert sample data for driver_team_assignments
INSERT INTO driver_team_assignments (id, driver_id, team_id, start_date, end_date) VALUES
('DTA01', 'TD01', 'DR001', '2024-01-01', NULL),
('DTA02', 'TD02', 'TB001', '2024-01-01', NULL),
('DTA03', 'TD03', 'TB001', '2024-01-01', NULL),
('DTA04', 'TD04', 'PF001', '2024-01-01', NULL),
('DTA05', 'TD05', 'DR001', '2024-01-01', NULL),
('DTA06', 'TD06', 'PF001', '2024-01-01', NULL),
('DTA07', 'TDJS', 'TA001', '2024-01-01', NULL),
('DTA08', 'TDCG', 'SR001', '2024-01-01', NULL),
('DTA09', 'TDMD', 'FW001', '2024-01-01', NULL),
('DTA10', 'TDTY', 'SR002', '2024-01-01', NULL),
('DTA11', 'TDNVA', 'SW001', '2024-01-01', NULL),
('DTA12', 'TDHM', 'RF001', '2024-01-01', NULL),
('DTA13', 'TDSC', 'GD001', '2024-01-01', NULL),
('DTA14', 'TDAR', 'SR001', '2024-01-01', NULL),
('DTA15', 'TDJP', 'TA001', '2024-01-01', NULL),
('DTA16', 'TDLT', 'BS001', '2024-01-01', NULL),
('DTA17', 'TDPK', 'TB001', '2024-01-01', NULL),
('DTA18', 'TDMK', 'SR002', '2024-01-01', NULL),
('DTA19', 'TDRL', 'LP001', '2024-01-01', NULL),
('DTA20', 'TDAK', 'RF001', '2024-01-01', NULL),
('DTA21', 'TDMS', 'PF001', '2024-01-01', NULL),
('DTA22', 'TDNO', 'BS001', '2024-01-01', NULL),
('DTA23', 'TDJT', 'ES001', '2024-01-01', NULL),
('DTA24', 'TDEM', 'SC001', '2024-01-01', NULL),

-- Các assignment cho mùa giải 2023
('DTA25', 'TD01', 'RF001', '2023-01-01', '2023-12-31'),
('DTA26', 'TD02', 'TA001', '2023-01-01', '2023-12-31'),
('DTA27', 'TD03', 'SR001', '2023-01-01', '2023-12-31'),
('DTA28', 'TD04', 'FW001', '2023-01-01', '2023-12-31'),
('DTA29', 'TD05', 'SR002', '2023-01-01', '2023-12-31'),
('DTA30', 'TD06', 'SW001', '2023-01-01', '2023-12-31'),
('DTA31', 'TDJS', 'BS001', '2023-01-01', '2023-12-31'),
('DTA32', 'TDCG', 'GD001', '2023-01-01', '2023-12-31'),
('DTA33', 'TDMD', 'SC001', '2023-01-01', '2023-12-31'),
('DTA34', 'TDTY', 'ES001', '2023-01-01', '2023-12-31'),
('DTA35', 'TDNVA', 'LP001', '2023-01-01', '2023-12-31'),

-- Các assignment cho mùa giải 2022
('DTA36', 'TDHM', 'SW001', '2022-01-01', '2022-12-31'),
('DTA37', 'TDSC', 'TB001', '2022-01-01', '2022-12-31'),
('DTA38', 'TDAR', 'FW001', '2022-01-01', '2022-12-31'),
('DTA39', 'TDJP', 'PF001', '2022-01-01', '2022-12-31'),
('DTA40', 'TDLT', 'DR001', '2022-01-01', '2022-12-31'),
('DTA41', 'TDPK', 'ES001', '2022-01-01', '2022-12-31'),
('DTA42', 'TDMK', 'SC001', '2022-01-01', '2022-12-31'),
('DTA43', 'TDRL', 'GD001', '2022-01-01', '2022-12-31'),
('DTA44', 'TDAK', 'BS001', '2022-01-01', '2022-12-31'),
('DTA45', 'TDMS', 'LP001', '2022-01-01', '2022-12-31');