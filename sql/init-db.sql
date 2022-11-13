create table users
(
    user_id text primary key,
    name    text
);

create table rooms
(
    id        serial primary key,
    name      text unique                                       not null,
    password  text,
    owner_id  text references users (user_id) on delete cascade not null,
    date      date check ( date > current_date ),
    max_price integer check ( max_price > 0 )
);

create table room_members
(
    room_id   integer references rooms (id) on delete cascade   not null,
    user_id   text references users (user_id) on delete cascade not null,
    recipient text references users (user_id) on delete cascade
);