CREATE TABLE country
(
    id         text NOT NULL
        CONSTRAINT country_pkey
            PRIMARY KEY,
    is_deleted bool NOT NULL DEFAULT false
);

CREATE TABLE city
(
    id         text NOT NULL
        CONSTRAINT city_pkey
            PRIMARY KEY,
    country_id text
        CONSTRAINT fk_city_country REFERENCES country (id),
    is_deleted bool NOT NULL DEFAULT false
);

create table role
(
    id           text    NOT NULL
        CONSTRAINT role_pkey
            PRIMARY KEY,
    name         text
        CONSTRAINT uk_role_name UNIQUE,
    priority     integer NOT NULL DEFAULT 1,
    is_deletable bool    NOT NULL DEFAULT true,
    is_deleted   bool    NOT NULL DEFAULT false
);

CREATE TABLE department
(
    id         text NOT NULL
        CONSTRAINT department_pkey
            PRIMARY KEY,
    name       text
        CONSTRAINT uk_department_name UNIQUE,
    head_id    text,
    is_deleted bool NOT NULL DEFAULT false
);

create table users
(
    id                  text NOT NULL
        CONSTRAINT users_pkey
            PRIMARY KEY,
    email               text NOT NULL
        CONSTRAINT uk_users_email UNIQUE,
    resource_manager_id text,
    department_id       text
        CONSTRAINT fk_users_department
            REFERENCES department (id),
    role_id             text
        CONSTRAINT fk_users_role
            REFERENCES role (id),
    status              text NOT NULL DEFAULT 'ACTIVE'
);

ALTER TABLE department
    ADD CONSTRAINT fk_department_users FOREIGN KEY (head_id) REFERENCES users (id);

CREATE TABLE activity
(
    id         text NOT NULL
        CONSTRAINT activity_pkey
            PRIMARY KEY,
    name       text NOT NULL
        CONSTRAINT uk_activity_name UNIQUE,
    is_deleted bool NOT NULL DEFAULT false
);

CREATE TABLE authority
(
    id   text
        CONSTRAINT authority_pkey PRIMARY KEY,
    name text NOT NULL
        CONSTRAINT uk_authority_name UNIQUE
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
    role_id      text
        CONSTRAINT fk_role_authority_role REFERENCES role (id),
    authority_id text
        CONSTRAINT fk_role_authority_authority REFERENCES authority (id)
);


CREATE TABLE event
(
    id        text PRIMARY KEY,
    type      text      NOT NULL,
    entity_id text,
    author    text
        CONSTRAINT fk_event_users REFERENCES users (id),
    context   text,
    time      timestamp NOT NULL,
    version   integer,
    payload   jsonb
);

CREATE INDEX cx_event_time ON event (time);
CLUSTER event USING cx_event_time;
