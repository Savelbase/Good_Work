create table comments
(
    id        text not null primary key,
    user_id   text not null,
    sender_id text not null,
    text      text,
    date_time timestamp,
    version   integer default 1
);

create table event
(
    id        text not null primary key,
    type      text not null,
    entity_id text,
    parent_id text,
    author    text,
    context   text,
    time      timestamp not null,
    version   integer,
    payload   jsonb
);

create index cx_event_time on event (time);
cluster event using cx_event_time;

create table unsent_event
(
    id        bigserial primary key,
    event_id  text constraint fk_unsent_event_event references event (id),
    entity_id text
);