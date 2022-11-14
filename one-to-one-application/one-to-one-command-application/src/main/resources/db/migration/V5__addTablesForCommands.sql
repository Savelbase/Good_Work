alter table users
    add column if not exists first_name text;

alter table users
    add column if not exists last_name text;

alter table users
    add column if not exists email text;

create table if not exists email_command
(
    id        text      not null
        constraint email_command_pkey
            primary key,
    type      text      not null,
    author    text,
    context   text,
    time      timestamp not null,
    payload   jsonb
);

create table if not exists unsent_email_command
(
    id        text      not null
        constraint unsent_email_command_pkey
            unique,
    command_id  text
        constraint fk_unsent_email_command_command
            references email_command
);

CREATE INDEX cx_email_command_time ON email_command (time);
CLUSTER email_command USING cx_email_command_time;