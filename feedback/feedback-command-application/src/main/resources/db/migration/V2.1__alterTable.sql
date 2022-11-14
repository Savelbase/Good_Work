alter table one_to_one drop constraint if exists fk_one_to_one_user;
alter table one_to_one drop constraint if exists fk_one_to_one_feedback_interval;

alter table one_to_one drop column feedback_interval_id;

alter table feedback_command
    add column one_to_one_id text
        constraint fk_feedback_one_to_one references one_to_one (id);

create table feedback_notification (
    id       text not null primary key,
    one_to_one_id text
        constraint fk_fd_notification_one_to_one references one_to_one (id),
    notification_counter integer default 0,
    rd_is_notified boolean default false,
    version  integer default 1
);
