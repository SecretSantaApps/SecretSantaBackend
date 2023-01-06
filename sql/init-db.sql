create table users
(
    user_id text primary key,
    name    text
);

create table rooms
(
    id           text primary key,
    name         text                                              not null,
    password     text                                              not null,
    owner_id     text references users (user_id) on delete cascade not null,
    date         date,
    max_price    integer check ( max_price > 0 ),
    game_started boolean default false
);

create table room_members
(
    room_id   text references rooms (id) on delete cascade not null,
    user_id   text references users (user_id)              not null,
    recipient text references users (user_id)
);