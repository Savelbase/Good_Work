create table if not exists roles
(
    id   serial not null
        constraint roles_pkey
            primary key,
    name varchar(20)
);

create table user_roles
(
    user_id varchar(255) not null
        constraint fkhfh9dx7w3ubf1co1vdev94g3f
            references users,
    role_id integer      not null
        constraint fkh8ciramu9cc9q3qcqiv4ue8a6
            references roles,
    constraint user_roles_pkey
        primary key (user_id, role_id)
);

create table refreshtoken
(
    id          varchar(255)      not null
        constraint refreshtoken_pkey
            primary key,
    expiry_date timestamp    not null,
    token       varchar(255) not null
        constraint uk_or156wbneyk8noo4jstv55ii3
            unique,
    user_id     varchar(255)
        constraint fka652xrdji49m4isx38pp4p80p
            references users
);