drop index if exists cx_event_time;
drop table if exists unsent_event;
drop table if exists event;

create index cx_comments_date_time on comments (date_time);
cluster comments using cx_comments_date_time;