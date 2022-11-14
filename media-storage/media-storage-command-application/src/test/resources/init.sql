CREATE TYPE IF NOT EXISTS "JSONB" AS json;
CREATE TYPE IF NOT EXISTS "text" AS VARCHAR(45);

CREATE TABLE IF NOT EXISTS media_storage
(
    id       VARCHAR(45) not null primary key,
    url     VARCHAR(45) not null,
    is_confirmed boolean not null default false,
    upload_date timestamp not null,
    user_id VARCHAR(45),
    version  int default 1
);

create table IF NOT EXISTS event
(
    id        VARCHAR(45)      not null
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
