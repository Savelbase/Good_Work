CREATE TABLE IF NOT EXISTS media_storage_query
(
    id       text not null primary key unique,
    url     text not null,
    is_confirmed boolean not null default false,
    upload_date timestamp not null,
    version  int default 1
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
    id        bigserial not null
        constraint unsent_event_id_key
            unique,
    event_id  text
        constraint fk_unsent_event_event
            references event,
    entity_id text
);

CREATE INDEX cx_event_time ON event (time);
CLUSTER event USING cx_event_time;