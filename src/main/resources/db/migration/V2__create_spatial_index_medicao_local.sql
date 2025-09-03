-- Índice espacial para consultas geográficas
CREATE SPATIAL INDEX idx_medicao_local_spatial ON medicao (`local`);