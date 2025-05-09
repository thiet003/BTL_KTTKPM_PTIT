CREATE DATABASE IF NOT EXISTS race_schedule_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE race_schedule_db;

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET collation_connection = utf8mb4_unicode_ci;

-- Create seasons table
CREATE TABLE IF NOT EXISTS seasons (
  id VARCHAR(36) PRIMARY KEY,
  year INT NOT NULL,
  name VARCHAR(255) NOT NULL,
  start_date DATE,
  end_date DATE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create circuits table
CREATE TABLE IF NOT EXISTS circuits (
  id VARCHAR(36) PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  location VARCHAR(255) NOT NULL,
  country VARCHAR(100) NOT NULL,
  length_km DOUBLE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create race_stages table
CREATE TABLE IF NOT EXISTS race_stages (
  id VARCHAR(36) PRIMARY KEY,
  season_id VARCHAR(36) NOT NULL,
  circuit_id VARCHAR(36) NOT NULL,
  name VARCHAR(255) NOT NULL,
  race_date DATE,
  laps INT NOT NULL,
  status VARCHAR(20) NOT NULL,
  description TEXT,
  FOREIGN KEY (season_id) REFERENCES seasons(id),
  FOREIGN KEY (circuit_id) REFERENCES circuits(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert sample data for seasons
INSERT INTO seasons (id, year, name, start_date, end_date) VALUES
('2024', 2024, 'Formula 1 2024 Season', '2024-03-02', '2024-12-08'),
('2023', 2023, 'Formula 1 2023 Season', '2023-03-05', '2023-11-26'),
('2022', 2022, 'Formula 1 2022 Season', '2022-03-20', '2022-11-20');

-- Insert sample data for circuits
INSERT INTO circuits (id, name, location, country, length_km) VALUES
('MONZA', 'Monza Circuit', 'Monza, Italy', 'Italy', 5.793),
('IMOLA', 'Autodromo Enzo e Dino Ferrari', 'Imola, Italy', 'Italy', 4.909),
('BARCELONA', 'Circuit de Barcelona-Catalunya', 'Montmeló, Spain', 'Spain', 4.675),
('HOCKENHEIM', 'Hockenheimring', 'Hockenheim, Germany', 'Germany', 4.574),
('MONACO', 'Circuit de Monaco', 'Monte Carlo, Monaco', 'Monaco', 3.337),
('SILVERSTONE', 'Silverstone Circuit', 'Silverstone, UK', 'UK', 5.891),
('SUZUKA', 'Suzuka International Racing Course', 'Suzuka, Japan', 'Japan', 5.807),
('SHANGHAI', 'Shanghai International Circuit', 'Shanghai, China', 'China', 5.451),
('MELBOURNE', 'Albert Park Circuit', 'Melbourne, Australia', 'Australia', 5.303),
('INTERLAGOS', 'Autódromo José Carlos Pace', 'Sao Paulo, Brazil', 'Brazil', 4.309),
('MONTREAL', 'Circuit Gilles Villeneuve', 'Montreal, Canada', 'Canada', 4.361),
('ASSEN', 'TT Circuit Assen', 'Assen, Netherlands', 'Netherlands', 4.555),
('SEPANG', 'Sepang International Circuit', 'Kuala Lumpur, Malaysia', 'Malaysia', 5.543),
('HANOI', 'Hanoi Street Circuit', 'Hanoi, Vietnam', 'Vietnam', 5.565);

-- Insert sample data for race_stages 2024 season
INSERT INTO race_stages (id, season_id, circuit_id, name, race_date, laps, status, description) VALUES
('GP01', '2024', 'MONZA', 'Italy Grand Prix', '2024-09-01', 54, 'FINISHED', 'Diễn ra tại đường đua lịch sử Monza, chặng đua này có nhiều khúc cua tốc độ cao và đòi hỏi kỹ thuật tốt từ các tay đua.'),
('GP02', '2024', 'IMOLA', 'Italy Plus Grand Prix', '2024-10-10', 60, 'ONGOING', 'Đường đua Imola mang tên hai huyền thoại Ferrari là Enzo và Dino Ferrari, nổi tiếng với độ khó và các khúc cua kỹ thuật.'),
('GP03', '2024', 'BARCELONA', 'Spain Grand Prix', '2024-08-05', 66, 'FINISHED', 'Barcelona là đường đua quen thuộc với nhiều đội đua do thường xuyên được sử dụng trong các cuộc thử nghiệm trước mùa giải.'),
('GP04', '2024', 'HOCKENHEIM', 'German Grand Prix', '2024-11-12', 67, 'NOT_STARTED', 'Hockenheimring là đường đua truyền thống của Đức với nhiều đoạn thẳng tốc độ cao xen kẽ các khúc cua kỹ thuật.'),
('GP05', '2024', 'MONACO', 'Monaco Grand Prix', '2024-05-26', 78, 'FINISHED', 'Là một trong những đường đua mang tính biểu tượng nhất trong thế giới F1, nổi tiếng với các khúc cua hẹp và cảnh quan tuyệt đẹp.'),
('GP06', '2024', 'SILVERSTONE', 'British Grand Prix', '2024-07-07', 52, 'FINISHED', 'Silverstone là nơi diễn ra chặng đua đầu tiên của giải F1 và vẫn là một đường đua mang tính lịch sử, thử thách kỹ năng tay lái.'),
('GP07', '2024', 'SUZUKA', 'Japanese Grand Prix', '2024-10-20', 53, 'NOT_STARTED', 'Suzuka được coi là một trong những đường đua kỹ thuật nhất, với hình số 8 độc đáo và khúc cua 130R nổi tiếng.'),
('GP08', '2024', 'SHANGHAI', 'Chinese Grand Prix', '2024-04-21', 56, 'FINISHED', 'Đường đua Thượng Hải được thiết kế hiện đại với nhiều khúc cua đa dạng từ chậm đến nhanh.'),
('GP09', '2024', 'HANOI', 'Vietnam Grand Prix', '2024-12-01', 55, 'NOT_STARTED', 'Đường đua mới tại Hà Nội kết hợp yếu tố đường phố và đường đua hiện đại, với nhiều khúc cua thú vị.'),

-- Insert sample data for race_stages 2023 season
('GP10', '2023', 'MONACO', 'Monaco Grand Prix', '2023-05-28', 78, 'FINISHED', 'Chặng đua lịch sử tại đường phố Monte Carlo với nhiều thách thức cho các tay đua.'),
('GP11', '2023', 'SILVERSTONE', 'British Grand Prix', '2023-07-16', 52, 'FINISHED', 'Đường đua kỹ thuật với lịch sử lâu đời, là nơi diễn ra chặng đua F1 đầu tiên.'),
('GP12', '2023', 'SUZUKA', 'Japanese Grand Prix', '2023-09-24', 53, 'FINISHED', 'Đường đua Suzuka với thiết kế độc đáo hình số 8, thử thách kỹ năng của các tay đua.'),
('GP13', '2023', 'INTERLAGOS', 'Brazilian Grand Prix', '2023-11-05', 71, 'FINISHED', 'Đường đua Interlagos nổi tiếng với khúc cua S của Senna và địa hình lên xuống thú vị.'),
('GP14', '2023', 'MONTREAL', 'Canadian Grand Prix', '2023-06-18', 70, 'FINISHED', 'Đường đua nằm trên đảo Notre-Dame, nổi tiếng với bức tường Champions và nhiều khu vực phanh mạnh.'),
('GP15', '2023', 'BARCELONA', 'Spanish Grand Prix', '2023-06-04', 66, 'FINISHED', 'Đường đua Catalunya là nơi quen thuộc với các đội đua do thường được sử dụng trong các buổi thử nghiệm.'),
('GP16', '2023', 'MELBOURNE', 'Australian Grand Prix', '2023-04-02', 58, 'FINISHED', 'Đường đua Albert Park kết hợp giữa đường đua thường và đường phố, tạo nên một thử thách thú vị.'),

-- Insert sample data for race_stages 2022 season
('GP17', '2022', 'MONZA', 'Italy Grand Prix', '2022-09-11', 53, 'FINISHED', 'Monza - Ngôi đền tốc độ, nơi các tay đua có thể đạt tốc độ cao nhất trong mùa giải.'),
('GP18', '2022', 'MONACO', 'Monaco Grand Prix', '2022-05-29', 78, 'FINISHED', 'Đường đua Monte Carlo đòi hỏi sự chính xác tuyệt đối và là chặng đua không thể bỏ lỡ trong lịch F1.'),
('GP19', '2022', 'SILVERSTONE', 'British Grand Prix', '2022-07-03', 52, 'FINISHED', 'Silverstone với nhiều góc cua tốc độ cao, là thử thách về khí động học cho các xe đua.'),
('GP20', '2022', 'SEPANG', 'Malaysian Grand Prix', '2022-10-02', 56, 'FINISHED', 'Đường đua Sepang nổi tiếng với thời tiết khắc nghiệt và nhiều khúc cua kỹ thuật.'),
('GP21', '2022', 'SHANGHAI', 'Chinese Grand Prix', '2022-04-10', 56, 'FINISHED', 'Đường đua Thượng Hải với khúc cua đầu tiên rất rộng và dài, là một thử thách độc đáo.'),
('GP22', '2022', 'ASSEN', 'Dutch Grand Prix', '2022-08-28', 72, 'FINISHED', 'TT Circuit Assen được biết đến với những khúc cua nhanh và dòng chảy liên tục.');