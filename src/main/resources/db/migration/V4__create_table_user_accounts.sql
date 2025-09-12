CREATE TABLE user_accounts (
  id BIGINT NOT NULL AUTO_INCREMENT,
  public_id BINARY(16) NOT NULL UNIQUE,
  username VARCHAR(80) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  created_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  created_by BIGINT,
  updated_at TIMESTAMP(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  updated_by BIGINT,
  deleted_at TIMESTAMP(6) NULL,
  deleted_by BIGINT,
  delete_reason VARCHAR(255),
  version BIGINT NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE user_account_roles (
  user_account_id BIGINT NOT NULL,
  role VARCHAR(20) NOT NULL,
  CONSTRAINT fk_user_roles_user FOREIGN KEY (user_account_id) REFERENCES user_accounts (id)
    ON DELETE CASCADE,
  INDEX ix_user_roles_user (user_account_id),
  INDEX ix_user_roles_role (role)
) ENGINE=InnoDB;
