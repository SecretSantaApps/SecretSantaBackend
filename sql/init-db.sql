create table users
(
    user_id text primary key,
    name    text
);

create table rooms
(
    name      text primary key                                  not null,
    password  text,
    owner_id  text references users (user_id) on delete cascade not null,
    date      date,
    max_price integer check ( max_price > 0 )
);

create table room_members
(
    room_name text references rooms (name) on delete cascade    not null,
    user_id   text references users (user_id) on delete cascade not null,
    recipient text references users (user_id) on delete cascade
);