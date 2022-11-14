CREATE TABLE IF NOT EXISTS users(
id          varchar(1024)   not null primary key unique,
username    varchar(100)    not null,
password    varchar(1024)    not null
)