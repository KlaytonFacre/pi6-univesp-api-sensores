CREATE TABLE samples (
  id BIGINT NOT NULL AUTO_INCREMENT,
  public_id BINARY(16) NOT NULL UNIQUE,

  sensor_id BIGINT NOT NULL,
  captured_at TIMESTAMP(6) NOT NULL,

  local POINT SRID 4326 NOT NULL,

  laeq DECIMAL(8,2) NOT NULL,
  lmax DECIMAL(8,2) NULL,
  lmin DECIMAL(8,2) NULL,
  window_seconds INT NOT NULL,

  created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  created_by BIGINT,
  updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  updated_by BIGINT,
  deleted_at TIMESTAMP(6) NULL,
  deleted_by BIGINT,
  delete_reason VARCHAR(255),
  version BIGINT NOT NULL,

  PRIMARY KEY (id),
  CONSTRAINT fk_samples_sensor FOREIGN KEY (sensor_id) REFERENCES sensors (id)
) ENGINE=InnoDB;

-- Índices para consultas eficientes
CREATE INDEX ix_samples_sensor_time ON samples (sensor_id, captured_at);
CREATE SPATIAL INDEX spx_samples_local ON samples (local);
