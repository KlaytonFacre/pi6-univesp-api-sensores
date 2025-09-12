CREATE TABLE owners (
  id BIGINT NOT NULL AUTO_INCREMENT,
  public_id BINARY(16) NOT NULL UNIQUE,
  name VARCHAR(120) NOT NULL,
  email VARCHAR(120) NOT NULL UNIQUE,
  phone VARCHAR(30),
  user_account_id BIGINT UNIQUE,
  created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  created_by BIGINT,
  updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  updated_by BIGINT,
  deleted_at TIMESTAMP(6) NULL,
  deleted_by BIGINT,
  delete_reason VARCHAR(255),
  version BIGINT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_owner_user_account FOREIGN KEY (user_account_id) REFERENCES user_accounts (id)
) ENGINE=InnoDB;

CREATE INDEX ix_owner_email ON owners (email);
