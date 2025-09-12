-- ============================================================
-- SEED: OWNERS
-- ============================================================
INSERT INTO owners (
  public_id,
  name,
  email,
  phone,
  created_at,
  updated_at,
  version
) VALUES
  (UUID_TO_BIN(UUID()), 'Owner One',   'owner1@example.com', '+55 11 99999-1111', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0),
  (UUID_TO_BIN(UUID()), 'Owner Two',   'owner2@example.com', '+55 21 98888-2222', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0),
  (UUID_TO_BIN(UUID()), 'Owner Three', 'owner3@example.com', '+55 31 97777-3333', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0);

-- ============================================================
-- SEED: SENSORS
-- (cada sensor associado a um owner via subselect no email)
-- ============================================================
INSERT INTO sensors (
  public_id,
  name,
  code,
  owner_id,
  serial_number,
  last_seen_at,
  status,
  created_at,
  updated_at,
  version
) VALUES
  (UUID_TO_BIN(UUID()), 'Sensor A', 'SEN-A1',
   (SELECT id FROM owners WHERE email = 'owner1@example.com'),
   'SN-A-001', CURRENT_TIMESTAMP(6), 'ACTIVE', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0),

  (UUID_TO_BIN(UUID()), 'Sensor B', 'SEN-B1',
   (SELECT id FROM owners WHERE email = 'owner2@example.com'),
   'SN-B-001', CURRENT_TIMESTAMP(6), 'INACTIVE', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0),

  (UUID_TO_BIN(UUID()), 'Sensor C', 'SEN-C1',
   (SELECT id FROM owners WHERE email = 'owner3@example.com'),
   'SN-C-001', CURRENT_TIMESTAMP(6), 'MAINTENANCE', CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0);

-- ============================================================
-- SEED: SAMPLES
-- (3 amostras de teste para cada sensor)
-- ============================================================
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
) VALUES
  -- Sensor A (São Paulo)
  (UUID_TO_BIN(UUID()), (SELECT id FROM sensors WHERE code = 'SEN-A1'),
   '2025-09-12 12:00:00', ST_SRID(POINT(-46.633308, -23.550520), 4326),
   65.3, 80.1, 50.2, 1, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0),

  (UUID_TO_BIN(UUID()), (SELECT id FROM sensors WHERE code = 'SEN-A1'),
   '2025-09-12 12:01:00', ST_SRID(POINT(-46.633400, -23.550600), 4326),
   70.5, 85.0, 55.0, 1, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0),

  (UUID_TO_BIN(UUID()), (SELECT id FROM sensors WHERE code = 'SEN-A1'),
   '2025-09-12 12:02:00', ST_SRID(POINT(-46.633500, -23.550700), 4326),
   68.0, 82.0, 52.0, 1, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0),

  -- Sensor B (Rio de Janeiro)
  (UUID_TO_BIN(UUID()), (SELECT id FROM sensors WHERE code = 'SEN-B1'),
   '2025-09-12 13:00:00', ST_SRID(POINT(-43.209372, -22.911014), 4326),
   60.0, 75.0, 48.0, 1, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0),

  (UUID_TO_BIN(UUID()), (SELECT id FROM sensors WHERE code = 'SEN-B1'),
   '2025-09-12 13:01:00', ST_SRID(POINT(-43.209400, -22.911050), 4326),
   62.0, 77.0, 49.0, 1, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0),

  (UUID_TO_BIN(UUID()), (SELECT id FROM sensors WHERE code = 'SEN-B1'),
   '2025-09-12 13:02:00', ST_SRID(POINT(-43.209450, -22.911100), 4326),
   63.5, 78.0, 50.0, 1, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0),

  -- Sensor C (Belo Horizonte)
  (UUID_TO_BIN(UUID()), (SELECT id FROM sensors WHERE code = 'SEN-C1'),
   '2025-09-12 14:00:00', ST_SRID(POINT(-43.934493, -19.935657), 4326),
   55.0, 70.0, 45.0, 1, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0),

  (UUID_TO_BIN(UUID()), (SELECT id FROM sensors WHERE code = 'SEN-C1'),
   '2025-09-12 14:01:00', ST_SRID(POINT(-43.934550, -19.935700), 4326),
   56.0, 72.0, 46.0, 1, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0),

  (UUID_TO_BIN(UUID()), (SELECT id FROM sensors WHERE code = 'SEN-C1'),
   '2025-09-12 14:02:00', ST_SRID(POINT(-43.934600, -19.935750), 4326),
   57.2, 73.0, 47.0, 1, CURRENT_TIMESTAMP(6), CURRENT_TIMESTAMP(6), 0);
