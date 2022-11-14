alter table users drop column is_rm;
alter table users rename column name to first_name;
alter table users rename column lastname to last_name;

alter table users
    add column email text constraint uk_users_email unique;
alter table users
    add column resource_manager_id text;
alter table users
    add column department_id text;
alter table users
    add column version integer default 1;

create table feedback_interval (
    id       text not null primary key,
    interval integer default 1,
    version  integer default 1
);

create table one_to_one (
    id                   text not null primary key,
    user_id              text
        constraint fk_one_to_one_user references users (id),
    resource_manager_id  text not null,
    date_time            timestamp,
    is_over              boolean default false,
    is_deleted           boolean default false,
    feedback_interval_id text
        constraint fk_one_to_one_feedback_interval references feedback_interval (id),
    version              integer default 1
);
