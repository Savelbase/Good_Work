-- Whitelist Email
TRUNCATE whitelist_email;

ALTER TABLE whitelist_email
    ADD CONSTRAINT uk_whitelist_email_email UNIQUE (email);

-- Refresh Token
TRUNCATE refresh_token;

ALTER TABLE refresh_token
    DROP CONSTRAINT fk_refresh_token_users;

SELECT hash::text, expiry_date, user_id::text
INTO _refresh_token
FROM refresh_token;
DROP TABLE refresh_token;
ALTER TABLE _refresh_token
    RENAME TO refresh_token;

ALTER TABLE refresh_token
    ADD CONSTRAINT refresh_token_pkey PRIMARY KEY (hash);

CREATE INDEX cx_refresh_token_expiry_date ON refresh_token (expiry_date);
CLUSTER refresh_token USING cx_refresh_token_expiry_date;


-- Users
TRUNCATE users;

ALTER TABLE users
    DROP CONSTRAINT fk_role_user;

ALTER TABLE users
    ALTER COLUMN role_id TYPE text;

SELECT id::text, email::text, password::text, role_id::text, login_attempts
INTO _users
FROM users;
DROP TABLE users;
ALTER TABLE _users
    RENAME TO users;

ALTER TABLE users
    ALTER COLUMN login_attempts SET DEFAULT 0;
ALTER TABLE users
    ADD COLUMN status text DEFAULT 'ACTIVE';

ALTER TABLE users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);
ALTER TABLE users
    ADD CONSTRAINT uk_user_email UNIQUE (email);

ALTER TABLE refresh_token
    ADD CONSTRAINT fk_refresh_token_users FOREIGN KEY (user_id) REFERENCES users (id);

-- Role
TRUNCATE role;

ALTER TABLE role
    ALTER COLUMN id TYPE text;

SELECT id::text
INTO _role
FROM role;
DROP TABLE role;
ALTER TABLE _role
    RENAME TO role;

ALTER TABLE role
    ADD COLUMN is_deleted bool DEFAULT false;
ALTER TABLE role
    ADD COLUMN priority integer DEFAULT 1;

ALTER TABLE role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);

ALTER TABLE users
    ADD CONSTRAINT fk_users_role FOREIGN KEY (role_id) REFERENCES role (id);


-- Authority
CREATE TABLE authority
(
    id   text PRIMARY KEY,
    name text UNIQUE
);

INSERT INTO authority
VALUES ('fb20b4f9-0806-479f-9d4d-c20bb7803ce2', 'AUTHORIZATION'),
       ('7aacf418-da6a-49b9-80e2-6fba98e2a103', 'EMPLOYEE_LIST'),
       ('c0d930e2-0747-4e12-884b-6000536ef1c6', 'EMPLOYEE_CARD'),
       ('3dc1c164-82da-4421-9e68-c4acb3c2cf15', 'USER_STATUS_SETTINGS'),
       ('41a15085-7b43-406a-8e8c-9c2952e93e45', 'ADD_EMPLOYEE'),
       ('bf1603dc-59a7-408e-90c0-cb1ec8ea89f8', 'ONE_TO_ONE'),
       ('6b3e5c5c-c277-4e42-8aa0-abc192137b6e', 'FEEDBACKS'),
       ('a046f4dd-1fee-41ed-8058-d05eb27ff526', 'ASSESSMENT_GOALS'),
       ('dbadb95a-d032-45d0-946a-920812d92b2d', 'COMMENTS'),
       ('e84a5f75-ab97-414c-ac0f-7f187cafc29e', 'ROLES_SETTINGS'),
       ('4872209a-f29d-4fe5-9244-27179c7fcb04', 'DEPARTMENTS_SETTINGS'),
       ('2597e5d8-7bd0-46bc-a17b-f810ecb0d37f', 'INTERVAL_SETTINGS');

CREATE TABLE role_authority
(
    role_id      text REFERENCES role (id),
    authority_id text REFERENCES authority (id)
);


CREATE TABLE event
(
    id        text PRIMARY KEY,
    type      text,
    entity_id text,
    author    text
        CONSTRAINT fk_event_users REFERENCES users (id),
    context   text,
    time      timestamp,
    version   integer,
    payload   jsonb
);

CREATE INDEX cx_event_time ON event (time);
CLUSTER event USING cx_event_time;

CREATE INDEX ix_event_entity_id ON event (entity_id);