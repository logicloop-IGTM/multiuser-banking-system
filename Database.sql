-- 1. Create the database
CREATE DATABASE IF NOT EXISTS bank_app
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;
  
USE bank_app;

-- 2. Accounts table: stores each account’s core data
CREATE TABLE accounts (
  account_number CHAR(6)      NOT NULL COMMENT 'Primary key, 6‑char UUID fragment',
  holder_name    VARCHAR(100) NOT NULL COMMENT 'Account owner’s name',
  account_type   ENUM('SAVINGS','CURRENT') NOT NULL DEFAULT 'SAVINGS',
  balance        DECIMAL(15,2) NOT NULL DEFAULT 0.00,
  created_at     DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (account_number)
) ENGINE=InnoDB;

-- 3. Transactions table: logs deposits and withdrawals
CREATE TABLE transactions (
  id               BIGINT        NOT NULL AUTO_INCREMENT,
  account_number   CHAR(6)       NOT NULL,
  transaction_type ENUM('DEPOSIT','WITHDRAWAL') NOT NULL,
  amount           DECIMAL(15,2) NOT NULL,
  occurred_at      DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  INDEX idx_acct    (account_number),
  CONSTRAINT fk_account
    FOREIGN KEY (account_number) REFERENCES accounts(account_number)
    ON DELETE CASCADE
    ON UPDATE CASCADE
) ENGINE=InnoDB;

-- 4. Optional: seed data (for testing)
INSERT INTO accounts (account_number, holder_name, account_type, balance)
VALUES
  ('A1B2C3', 'Alice Kumar', 'SAVINGS', 5000.00),
  ('D4E5F6', 'Ravi Singh', 'CURRENT', 1500.00);

INSERT INTO transactions (account_number, transaction_type, amount)
VALUES
  ('A1B2C3', 'DEPOSIT', 5000.00),
  ('D4E5F6', 'DEPOSIT', 2000.00),
  ('D4E5F6', 'WITHDRAWAL', 500.00);

-- 5. Access patterns
--   * To get an account’s balance:
--       SELECT balance FROM accounts WHERE account_number = 'A1B2C3';
--   * To log a new transaction and update the balance:
--     START TRANSACTION;
--       INSERT INTO transactions (account_number, transaction_type, amount)
--         VALUES ('A1B2C3','DEPOSIT', 250.00);
--       UPDATE accounts
--         SET balance = balance + 250.00
--         WHERE account_number = 'A1B2C3';
--     COMMIT;

