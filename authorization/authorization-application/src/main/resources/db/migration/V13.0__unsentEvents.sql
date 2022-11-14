CREATE TABLE unsent_event
(
    id       BIGSERIAL NOT NULL UNIQUE,
    event_id text
        CONSTRAINT fk_unsent_event_event REFERENCES event (id)
);