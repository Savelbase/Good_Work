create index cx_feedback_date_time on feedback_query (date_time);
cluster feedback_query using cx_feedback_date_time;

alter table event drop constraint if exists cx_event_time;
drop table if exists event, unsent_event cascade;

alter table users drop column name;
alter table users drop column lastname;
alter table users drop column is_rm;

alter table users
    add column role text not null default 'EMPLOYEE';
alter table users
    add column version integer default 1;
