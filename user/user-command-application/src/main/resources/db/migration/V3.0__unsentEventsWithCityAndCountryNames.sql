ALTER TABLE city
    DROP CONSTRAINT fk_city_country;
TRUNCATE city;
ALTER TABLE city
    ADD COLUMN name text NOT NULL;

TRUNCATE country;
ALTER TABLE country
    ADD COLUMN name text NOT NULL
        CONSTRAINT uk_country_name UNIQUE;
ALTER TABLE city
    ADD CONSTRAINT fk_city_country FOREIGN KEY (country_id) REFERENCES country (id);


CREATE TABLE unsent_event
(
    id       BIGSERIAL NOT NULL UNIQUE,
    event_id text
        CONSTRAINT fk_unsent_event_event REFERENCES event (id)
);