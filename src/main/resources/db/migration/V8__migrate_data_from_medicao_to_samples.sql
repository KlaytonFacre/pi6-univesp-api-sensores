INSERT INTO samples (
  public_id,
  sensor_id,
  captured_at,
  local,
  laeq,
  lmax,
  lmin,
  window_seconds,
  created_at,
  updated_at,
  version
)
SELECT
  UUID_TO_BIN(UUID()),            -- gera public_id (BINARY(16))
  m.sensor_id,
  m.`timestamp`,
  m.`local`,
  CAST(m.noise_db AS DECIMAL(8,2)) AS laeq,
  NULL AS lmax,
  NULL AS lmin,
  1    AS window_seconds,         -- ajuste aqui se necessário
  CURRENT_TIMESTAMP(6) AS created_at,
  CURRENT_TIMESTAMP(6) AS updated_at,
  0    AS version
FROM medicao m;
