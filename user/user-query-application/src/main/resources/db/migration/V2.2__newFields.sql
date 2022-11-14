ALTER TABLE department
    ADD COLUMN head_first_name text;
ALTER TABLE department
    ADD COLUMN head_last_name text;

ALTER TABLE department DROP COLUMN members;

ALTER TABLE users DROP CONSTRAINT fk_users_country;
ALTER TABLE users DROP CONSTRAINT fk_users_city;
ALTER TABLE users ADD COLUMN country_name TEXT;
ALTER TABLE users ADD COLUMN city_name TEXT;


ALTER TABLE users
    ALTER COLUMN activities SET DEFAULT '[]'::jsonb;
ALTER TABLE role
    ALTER COLUMN authorities SET DEFAULT '[]'::jsonb;