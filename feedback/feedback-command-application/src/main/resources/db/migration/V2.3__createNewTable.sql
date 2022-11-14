create table department
(
    id             text not null primary key,
    name           text,
    head_id        text,
    deleted bool   not null default false,
    deletable bool default true
);

create table if not exists email_command
(
    id        text not null primary key,
    type      text not null,
    context   text,
    time      timestamp not null,
    payload   jsonb
);

create table if not exists unsent_email_command
(
    id         text not null unique,
    command_id text
        constraint fk_unsent_email_command_command
            references email_command
);

create index cx_email_command_time on email_command (time);
cluster email_command using cx_email_command_time;