create table if not exists email
(
    id        text      not null
        constraint email_pkey
            primary key,
    email_to            text not null,
    subject             text not null,
    text                text not null,
    sending_date_time   timestamp not null
);

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

create table whitelist_email
(
    id    text not null
        constraint whitelist_email_pk
            primary key,
    email text
);

ALTER TABLE whitelist_email
    ADD CONSTRAINT uk_whitelist_email_email UNIQUE (email);