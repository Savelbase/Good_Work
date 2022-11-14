ALTER TABLE users
    DROP COLUMN name;
ALTER TABLE users
    DROP COLUMN last_name;
ALTER TABLE users
    DROP COLUMN email;
ALTER TABLE users
    DROP COLUMN is_rm;

ALTER TABLE users
    ADD COLUMN resource_manager_id text;