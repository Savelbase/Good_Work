create table feedback_query
(
    id                   text not null primary key,
    date_time            timestamp,
    rm_id                text not null,
    user_id              text not null,
    overall_assessment   integer default 3,
    professional_skills  integer default 3,
    work_quality         integer default 3,
    critical_thinking    integer default 3,
    reliability          integer default 3,
    communication_skills integer default 3,
    department_comment   text,
    development_comment  text,
    goals_comment        text,
    project_comment      text,
    activities_comment   text,
    additionally_comment text,
    version              integer default 1
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

create table users
(
    id       text not null primary key,
    name     text,
    lastname text,
    is_rm boolean default false

);

CREATE INDEX cx_event_time ON event (time);
CLUSTER event USING cx_event_time;