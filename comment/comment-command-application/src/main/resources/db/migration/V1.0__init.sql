create table comments
(
    id        text not null primary key,
    user_id   text not null,
    sender_id text not null,
    text      text,
    date_time timestamp,
    version   integer default 1,
    deleted   boolean
);
CREATE TABLE event
(
    id        text PRIMARY KEY,
    type      text      NOT NULL,
    entity_id text,
    parent_id text,
    author    text,
    context   text,
    time      timestamp NOT NULL,
    version   integer default 1,
    payload   jsonb
);

CREATE INDEX cx_event_time ON event (time);
CLUSTER event USING cx_event_time;

CREATE TABLE unsent_event
(
    id        BIGSERIAL PRIMARY KEY,
    event_id  text
        CONSTRAINT fk_unsent_event_event REFERENCES event (id),
    entity_id text
);