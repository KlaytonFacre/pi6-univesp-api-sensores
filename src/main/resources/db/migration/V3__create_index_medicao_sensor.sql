-- Índice comum para otimizar buscas por sensor
CREATE INDEX idx_medicao_sensor ON medicao (sensor_id);