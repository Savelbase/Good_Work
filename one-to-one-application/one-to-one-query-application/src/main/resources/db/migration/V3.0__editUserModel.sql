ALTER TABLE users
    DROP COLUMN email;
ALTER TABLE users
    DROP COLUMN is_rm;

ALTER TABLE users
    RENAME COLUMN name TO first_name;