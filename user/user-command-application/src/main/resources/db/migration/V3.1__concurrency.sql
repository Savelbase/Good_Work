ALTER TABLE unsent_event
    ADD COLUMN entity_id text;


ALTER TABLE role
    ADD COLUMN authorities text[];

DROP TABLE role_authority;
DROP TABLE authority;


ALTER TABLE activity
    ADD COLUMN version int DEFAULT 1;
ALTER TABLE city
    ADD COLUMN version int DEFAULT 1;
ALTER TABLE country
    ADD COLUMN version int DEFAULT 1;
ALTER TABLE department
    ADD COLUMN version int DEFAULT 1;
ALTER TABLE role
    ADD COLUMN version int DEFAULT 1;
ALTER TABLE users
    ADD COLUMN version int DEFAULT 1;
