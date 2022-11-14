-- Delete test data
DELETE
FROM user_roles
WHERE user_id = 'a487f7fa-e658-11eb-ba80-0242ac130004';

DELETE
FROM users
WHERE id = 'a487f7fa-e658-11eb-ba80-0242ac130004';


-- Users
ALTER TABLE users
    DROP COLUMN username;

ALTER TABLE users
    ALTER COLUMN id TYPE VARCHAR(36);
ALTER TABLE users
    ALTER COLUMN email TYPE VARCHAR(36);
ALTER TABLE users
    ALTER COLUMN password TYPE VARCHAR(60);

ALTER TABLE users
    ADD CONSTRAINT uk_user_email UNIQUE (email);


-- RefreshToken
TRUNCATE refreshtoken;

ALTER TABLE refreshtoken
    RENAME TO refresh_token;

ALTER TABLE refresh_token
    DROP COLUMN token;
ALTER TABLE refresh_token
    DROP COLUMN id;

ALTER TABLE refresh_token
    RENAME CONSTRAINT fka652xrdji49m4isx38pp4p80p TO fk_refresh_token_users;

ALTER TABLE refresh_token
    ALTER COLUMN user_id TYPE VARCHAR(36);

ALTER TABLE refresh_token
    ADD COLUMN hash VARCHAR(64) PRIMARY KEY;

CREATE INDEX idx_refresh_token_expiry_date ON refresh_token (expiry_date);


-- Role
DROP TABLE user_roles;

ALTER TABLE roles
    RENAME TO role;

ALTER TABLE users
    ADD COLUMN role_id INTEGER
        CONSTRAINT fk_role_user REFERENCES role;

ALTER TABLE role
    RENAME CONSTRAINT roles_pkey TO role_pkey;

ALTER TABLE role
    ADD CONSTRAINT uk_role_name UNIQUE (name);
