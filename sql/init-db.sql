create table rooms
(
    id        serial primary key,
    name      text not null,
    password  text,
    date      date check ( date > current_date ),
    max_price integer check ( max_price > 0 )
);

create table users
(
    user_id integer primary key,
    name    text
);

create table room_members
(
    room_id   integer references rooms (id),
    user_id   integer references users (user_id),
    recipient integer references users (user_id)
);