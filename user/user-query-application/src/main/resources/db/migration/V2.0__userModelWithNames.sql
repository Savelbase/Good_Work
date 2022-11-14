ALTER TABLE users
    ADD CONSTRAINT fk_users_country FOREIGN KEY (country_id) REFERENCES country (id);

ALTER TABLE users
    ADD CONSTRAINT fk_users_city FOREIGN KEY (city_id) REFERENCES city (id);

ALTER TABLE users
    ADD CONSTRAINT fk_users_department FOREIGN KEY (department_id) REFERENCES department (id);

ALTER TABLE users
    ADD COLUMN resource_manager_first_name text;
ALTER TABLE users
    ADD COLUMN resource_manager_last_name text;

ALTER TABLE users
    DROP COLUMN activities_ids;
ALTER TABLE users
    ADD COLUMN activities jsonb;

ALTER TABLE role DROP COLUMN authorities;
ALTER TABLE role ADD COLUMN authorities jsonb;