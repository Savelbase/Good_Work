create table whitelist_email
(
    id    text not null
        constraint whitelist_email_pk
            primary key,
    email text
);