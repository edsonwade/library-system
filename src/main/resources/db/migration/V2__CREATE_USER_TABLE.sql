create table if not exists users
(
    id       serial primary key not null,
    login    text               not null unique,
    password text               not null,
    role     text               not null


);