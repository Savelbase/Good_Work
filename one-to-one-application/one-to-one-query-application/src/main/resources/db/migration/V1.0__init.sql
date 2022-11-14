CREATE TABLE IF NOT EXISTS one_to_one_query
(
    id                  text not null primary key unique,
    comment             text,
    date_time           timestamp not null,
    is_over             boolean default false,
    resource_manager_id text not null ,
    user_id             text not null ,
    is_deleted          boolean default false,
    version             int DEFAULT 1
);

create table users
(
    id       text not null primary key,
    name     text,
    last_name text,
    email     text,
    is_rm     boolean default false
);

create table event
(
    id        text      not null
        constraint event_pkey
            primary key,
    type      text      not null,
    entity_id text,
    author    text,
    context   text,
    time      timestamp not null,
    version   integer,
    payload   jsonb,
    parent_id text
);

create table unsent_event
(
    id        bigserial
        constraint unsent_event_id_key
            unique,
    event_id  text
        constraint fk_unsent_event_event
            references event,
    entity_id text
);

CREATE INDEX cx_event_time ON event (time);
CLUSTER event USING cx_event_time;